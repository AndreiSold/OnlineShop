package ro.msg.learning.shop.odata;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Note: edm is the abbreviation for Entity Data Model.
 * Accordingly, we understand that the CsdlEdmProvider is supposed to provide static descriptive information.
 */
@Component
public class CustomEdmProvider extends CsdlAbstractEdmProvider {

    // Service Namespace
    private static final String NAMESPACE = "OData.Demo";

    // EDM Container
    private static final String CONTAINER_NAME = "Container";
    private static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    // Entity Types Names
    public static final String ET_PRODUCT_NAME = "Product";
    public static final FullQualifiedName ET_PRODUCT_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_NAME);


    public static final String ET_ORDER_NAME = "Order";
    public static final FullQualifiedName ET_ORDER_FQN = new FullQualifiedName(NAMESPACE, ET_ORDER_NAME);

    public static final String ET_ORDER_DETAILS_NAME = "OrderDetail";
    public static final FullQualifiedName ET_ORDER_DETAILS_FQN = new FullQualifiedName(NAMESPACE, ET_ORDER_DETAILS_NAME);


    // Entity Set Names
    public static final String ES_PRODUCTS_NAME = "Products";
    public static final String ES_ORDERS_NAME = "Orders";
    public static final String ES_ORDERS_DETAILS_NAME = "OrderDetails";

    /**
     * getEntityType() Here we declare the EntityType “Product” and a few of its properties
     */
    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {
        // this method is called for one of the EntityTypes that are configured in the Schema
        if (entityTypeName.equals(ET_PRODUCT_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("ID").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty name = new CsdlProperty().setName("Name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty description = new CsdlProperty().setName("Description").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());


            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("ID");

            CsdlNavigationProperty navProp = new CsdlNavigationProperty()
                .setName(ES_ORDERS_DETAILS_NAME)
                .setType(ET_ORDER_DETAILS_FQN)
                .setCollection(true)
                .setPartner(ET_PRODUCT_NAME);
            List<CsdlNavigationProperty> navPropList = new ArrayList<>();
            navPropList.add(navProp);

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_PRODUCT_NAME);
            entityType.setProperties(Arrays.asList(id, name, description));
            entityType.setKey(Collections.singletonList(propertyRef));
            entityType.setNavigationProperties(navPropList);

            return entityType;
        }
        if (entityTypeName.equals(ET_ORDER_FQN)) {
            CsdlProperty id = new CsdlProperty().setName("ID").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty address = new CsdlProperty().setName("Address").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("ID");


            CsdlNavigationProperty navProp = new CsdlNavigationProperty()
                .setName(ES_ORDERS_DETAILS_NAME)
                .setType(ET_ORDER_DETAILS_FQN)
                .setCollection(true)
                .setPartner(ET_ORDER_NAME);
            List<CsdlNavigationProperty> navPropList = new ArrayList<>();
            navPropList.add(navProp);


            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_ORDER_NAME);
            entityType.setProperties(Arrays.asList(id, address));
            entityType.setKey(Collections.singletonList(propertyRef));
            entityType.setNavigationProperties(navPropList);

            return entityType;

        }
        if (entityTypeName.equals(ET_ORDER_DETAILS_FQN)) {
            CsdlProperty id = new CsdlProperty().setName("ID").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty quantity = new CsdlProperty().setName("Quantity").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("ID");

            CsdlNavigationProperty navProp = new CsdlNavigationProperty()
                .setName(ET_PRODUCT_NAME)
                .setType(ET_PRODUCT_FQN)
                .setNullable(false)
                .setPartner(ES_ORDERS_DETAILS_NAME);

            CsdlNavigationProperty navProp2 = new CsdlNavigationProperty()
                .setName(ET_ORDER_NAME)
                .setType(ET_ORDER_FQN)
                .setNullable(false)
                .setPartner(ES_ORDERS_DETAILS_NAME);

            List<CsdlNavigationProperty> navPropList = new ArrayList<>();
            navPropList.add(navProp);
            navPropList.add(navProp2);

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_ORDER_DETAILS_NAME);
            entityType.setProperties(Arrays.asList(id, quantity));
            entityType.setKey(Collections.singletonList(propertyRef));
            entityType.setNavigationProperties(navPropList);

            return entityType;

        }

        return null;
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {

        CsdlEntitySet entitySet = new CsdlEntitySet();
        if (createNavigationToOrderDetails(entityContainer, entitySetName, entitySet, ES_PRODUCTS_NAME, ET_PRODUCT_FQN, ET_ORDER_DETAILS_NAME))
            return entitySet;

        if (createNavigationToOrderDetails(entityContainer, entitySetName, entitySet, ES_ORDERS_NAME, ET_ORDER_FQN, ES_ORDERS_DETAILS_NAME))
            return entitySet;

        if (entityContainer.equals(CONTAINER) && entitySetName.equals(ES_ORDERS_DETAILS_NAME)) {
            entitySet.setName(ES_ORDERS_DETAILS_NAME);
            entitySet.setType(ET_ORDER_DETAILS_FQN);

            CsdlNavigationPropertyBinding navPropBinding2 = new CsdlNavigationPropertyBinding();
            navPropBinding2.setPath(ET_ORDER_NAME); // the path from entity type to navigation property
            navPropBinding2.setTarget(ES_ORDERS_NAME);

            CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
            navPropBinding.setPath(ET_PRODUCT_NAME); // the path from entity type to navigation property
            navPropBinding.setTarget(ES_PRODUCTS_NAME); //target entitySet, where the nav prop points to

            List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<>();
            navPropBindingList.add(navPropBinding);
            navPropBindingList.add(navPropBinding2);

            entitySet.setNavigationPropertyBindings(navPropBindingList);

            return entitySet;
        }
        return null;
    }

    private boolean createNavigationToOrderDetails(FullQualifiedName entityContainer, String entitySetName, CsdlEntitySet entitySet, String esName, FullQualifiedName etFqn, String etOrderDetailsName) {
        if (entityContainer.equals(CONTAINER) && entitySetName.equals(esName)) {

            entitySet.setName(esName);
            entitySet.setType(etFqn);

            CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
            navPropBinding.setTarget(ES_ORDERS_DETAILS_NAME);//target entitySet, where the nav prop points to
            navPropBinding.setPath(etOrderDetailsName); // the path from entity type to navigation property
            List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<>();
            navPropBindingList.add(navPropBinding);
            entitySet.setNavigationPropertyBindings(navPropBindingList);

            return true;
        }
        return false;
    }

    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {
        // This method is invoked when displaying the Service Document at e.g. http://localhost:8080/DemoService/DemoService.svc
        if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
            CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
            entityContainerInfo.setContainerName(CONTAINER);
            return entityContainerInfo;
        }

        return null;
    }

    @Override
    public List<CsdlSchema> getSchemas() {
        // create Schema
        CsdlSchema schema1 = new CsdlSchema();
        schema1.setNamespace(NAMESPACE);

        // add EntityTypes
        List<CsdlEntityType> entityTypes = new ArrayList<>();
        entityTypes.add(getEntityType(ET_PRODUCT_FQN));

        entityTypes.add(getEntityType(ET_ORDER_FQN));

        entityTypes.add(getEntityType(ET_ORDER_DETAILS_FQN));

        schema1.setEntityTypes(entityTypes);

        // add EntityContainer
        schema1.setEntityContainer(getEntityContainer());

        // finally
        List<CsdlSchema> schemas = new ArrayList<>();
        schemas.add(schema1);

        return schemas;
    }

    @Override
    public CsdlEntityContainer getEntityContainer() {
        // create EntitySets
        List<CsdlEntitySet> entitySets = new ArrayList<>();
        entitySets.add(getEntitySet(CONTAINER, ES_PRODUCTS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_ORDERS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_ORDERS_DETAILS_NAME));
        // create EntityContainer
        CsdlEntityContainer entityContainer = new CsdlEntityContainer();
        entityContainer.setName(CONTAINER_NAME);
        entityContainer.setEntitySets(entitySets);

        return entityContainer;
    }


}
