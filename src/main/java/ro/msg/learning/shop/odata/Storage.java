package ro.msg.learning.shop.odata;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class Storage {

    private List<Entity> productList;

    private ProductRepository productRepository;

    public Storage(ProductRepository productRepository) {
        productList = new ArrayList<>();
        this.productRepository = productRepository;
        initSampleData();
    }

    /* PUBLIC FACADE */
    public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet) {

        // actually, this is only required if we have more than one Entity Sets
        if (edmEntitySet.getName().equals(DemoEdmProvider.ES_PRODUCTS_NAME)) {
            return getProducts();
        }

        return null;
    }

    public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException {

        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        // actually, this is only required if we have more than one Entity Type
        if (edmEntityType.getName().equals(DemoEdmProvider.ET_PRODUCT_NAME)) {
            return getProduct(edmEntityType, keyParams);
        }

        return null;
    }

    private EntityCollection getProducts() {
        EntityCollection retEntitySet = new EntityCollection();

        for (Entity productEntity : this.productList) {
            retEntitySet.getEntities().add(productEntity);
        }

        return retEntitySet;
    }

    private Entity getProduct(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException {

        // the list of entities at runtime
        EntityCollection entitySet = getProducts();

        /*  generic approach  to find the requested entity */
        Entity requestedEntity = Util.findEntity(edmEntityType, entitySet, keyParams);

        if (requestedEntity == null) {
            // this variable is null if our data doesn't contain an entity for the requested key
            // Throw suitable exception
            throw new ODataApplicationException("Entity for requested key doesn't exist",
                HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        return requestedEntity;
    }

    private void initSampleData() {

        List<Product> productListDb = productRepository.findAll();

        productListDb.forEach(product -> {

            final Entity entity = new Entity()
                .addProperty(new Property(null, "id", ValueType.PRIMITIVE, product.getId()))
                .addProperty(new Property(null, "name", ValueType.PRIMITIVE, product.getName()))
                .addProperty(new Property(null, "price", ValueType.PRIMITIVE, product.getPrice()))
                .addProperty(new Property(null, "weight", ValueType.PRIMITIVE, product.getWeight()))
                .addProperty(new Property("Supplier", "suppliers", ValueType.COLLECTION_ENTITY, product.getSuppliers()))
                .addProperty(new Property(null, "description", ValueType.PRIMITIVE, product.getDescription()));
            entity.setId(createId("Product" + product.getId(), product.getId()));
            productList.add(entity);
        });
    }

    private URI createId(String entitySetName, Object id) {
        try {
            return new URI(entitySetName + "(" + id + ")");
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
        }
    }
}
