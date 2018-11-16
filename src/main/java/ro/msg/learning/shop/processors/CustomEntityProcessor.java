package ro.msg.learning.shop.processors;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.ContextURL.Suffix;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.*;
import ro.msg.learning.shop.odata.CustomUtil;
import ro.msg.learning.shop.odata.Storage;

import java.util.List;
import java.util.Locale;

public class CustomEntityProcessor implements EntityProcessor {

    private OData odata;
    private ServiceMetadata srvMetadata;
    private Storage storage;

    public CustomEntityProcessor(Storage storage) {
        this.storage = storage;
    }

    public void init(OData odata, ServiceMetadata serviceMetadata) {
        this.odata = odata;
        this.srvMetadata = serviceMetadata;
    }

    public void readEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
        throws ODataApplicationException, SerializerException {

        EdmEntityType responseEdmEntityType = null; // we'll need this to build the ContextURL
        Entity responseEntity = null; // required for serialization of the response body
        EdmEntitySet responseEdmEntitySet = null; // we need this for building the contextUrl

        // 1st step: retrieve the requested Entity: can be "normal" read operation, or navigation (to-one)
        List<UriResource> resourceParts = uriInfo.getUriResourceParts();
        int segmentCount = resourceParts.size();

        UriResource uriResource = resourceParts.get(0); // in our example, the first segment is the EntitySet
        if (!(uriResource instanceof UriResourceEntitySet)) {
            throw new ODataApplicationException("Only EntitySet is supported",
                HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        }

        UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) uriResource;
        EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();

        // Analyze the URI segments
        if (segmentCount == 1) { // no navigation
            responseEdmEntityType = startEdmEntitySet.getEntityType();
            responseEdmEntitySet = startEdmEntitySet; // since we have only one segment

            // 2. step: retrieve the data from backend
            List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
            responseEntity = storage.readEntityData(startEdmEntitySet, keyPredicates);
        } else if (segmentCount == 2) { // navigation
            UriResource navSegment = resourceParts.get(1); // in our example we don't support more complex URIs
            if (navSegment instanceof UriResourceNavigation) {
                UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) navSegment;
                EdmNavigationProperty edmNavigationProperty = uriResourceNavigation.getProperty();
                responseEdmEntityType = edmNavigationProperty.getType();
                // contextURL displays the last segment
                responseEdmEntitySet = CustomUtil.getNavigationTargetEntitySet(startEdmEntitySet, edmNavigationProperty);

                // 2nd: fetch the data from backend.
                // e.g. for the URI: Products(1)/Category we have to find the correct Category entity
                List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
                // e.g. for Products(1)/Category we have to find first the Products(1)
                Entity sourceEntity = storage.readEntityData(startEdmEntitySet, keyPredicates);

                // now we have to check if the navigation is
                // a) to-one: e.g. Products(1)/Category
                // b) to-many with key: e.g. Categories(3)/Products(5)
                // the key for nav is used in this case: Categories(3)/Products(5)
                List<UriParameter> navKeyPredicates = uriResourceNavigation.getKeyPredicates();

                if (navKeyPredicates.isEmpty()) { // e.g. DemoService.svc/Products(1)/Category
                    responseEntity = storage.getRelatedEntity(sourceEntity, responseEdmEntityType);
                } else { // e.g. DemoService.svc/Categories(3)/Products(5)
                    responseEntity = storage.getRelatedEntity(sourceEntity, responseEdmEntityType, navKeyPredicates);
                }
            }
        } else {
            // this would be the case for e.g. Products(1)/Category/Products(1)/Category
            throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
        }

        if (responseEntity == null) {
            // this is the case for e.g. DemoService.svc/Categories(4) or DemoService.svc/Categories(3)/Products(999)
            throw new ODataApplicationException("Nothing found.", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ROOT);
        }

        // 3. serialize
        ContextURL contextUrl = ContextURL.with().entitySet(responseEdmEntitySet).suffix(Suffix.ENTITY).build();
        EntitySerializerOptions opts = EntitySerializerOptions.with().contextURL(contextUrl).build();

        ODataSerializer serializer = this.odata.createSerializer(responseFormat);
        SerializerResult serializerResult = serializer.entity(this.srvMetadata,
            responseEdmEntityType, responseEntity, opts);

        // 4. configure the response object
        response.setContent(serializerResult.getContent());
        response.setStatusCode(HttpStatusCode.OK.getStatusCode());
        response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
    }

    /*
     * These processor methods are not handled in this tutorial
     */

    public void createEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo,
                             ContentType requestFormat, ContentType responseFormat)
        throws ODataApplicationException {
        throw new ODataApplicationException("Not supported.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
    }

    public void updateEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo,
                             ContentType requestFormat, ContentType responseFormat)
        throws ODataApplicationException {
        throw new ODataApplicationException("Not supported.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
    }

    public void deleteEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo)
        throws ODataApplicationException {
        throw new ODataApplicationException("Not supported.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
    }
}