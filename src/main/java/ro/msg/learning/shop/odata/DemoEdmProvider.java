package ro.msg.learning.shop.odata;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DemoEdmProvider extends CsdlAbstractEdmProvider {

    // Service Namespace
    public static final String NAMESPACE = "OData.Demo";

    // EDM Container
    public static final String CONTAINER_NAME = "Container";
    public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    // PRODUCT
    public static final String ET_PRODUCT_NAME = "Product";
    public static final FullQualifiedName ET_PRODUCT_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_NAME);

    // ORDER
    public static final String ET_ORDER_NAME = "Order";
    public static final FullQualifiedName ET_ORDER_FQN = new FullQualifiedName(NAMESPACE, ET_ORDER_NAME);

    // PRODUCT CATEGORIE
    public static final String ET_PRODUCT_CATEGORY_NAME = "ProductCategory";
    public static final FullQualifiedName ET_PRODUCT_CATEGORY_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_CATEGORY_NAME);

    // SUPPLIER
    public static final String ET_SUPPLIER_NAME = "Supplier";
    public static final FullQualifiedName ET_SUPPLIER_FQN = new FullQualifiedName(NAMESPACE, ET_SUPPLIER_NAME);

    // STOCK
    public static final String ET_STOCK_NAME = "Stock";
    public static final FullQualifiedName ET_STOCK_FQN = new FullQualifiedName(NAMESPACE, ET_STOCK_NAME);

    // ORDER DETAIL
    public static final String ET_ORDER_DETAIL_NAME = "OrderDetail";
    public static final FullQualifiedName ET_ORDER_DETAIL_FQN = new FullQualifiedName(NAMESPACE, ET_ORDER_DETAIL_NAME);

    // LOCATION
    public static final String ET_LOCATION_NAME = "Location";
    public static final FullQualifiedName ET_LOCATION_FQN = new FullQualifiedName(NAMESPACE, ET_LOCATION_NAME);

    // REVENUE
    public static final String ET_REVENUE_NAME = "Revenue";
    public static final FullQualifiedName ET_REVENUE_FQN = new FullQualifiedName(NAMESPACE, ET_REVENUE_NAME);

    // CUSTOMER
    public static final String ET_CUSTOMER_NAME = "Customer";
    public static final FullQualifiedName ET_CUSTOMER_FQN = new FullQualifiedName(NAMESPACE, ET_CUSTOMER_NAME);

    // ROLE
    public static final String ET_ROLE_NAME = "Role";
    public static final FullQualifiedName ET_ROLE_FQN = new FullQualifiedName(NAMESPACE, ET_ROLE_NAME);

    // ADDRESS
    public static final String ET_ADDRESS_NAME = "Address";
    public static final FullQualifiedName ET_ADDRESS_FQN = new FullQualifiedName(NAMESPACE, ET_ADDRESS_NAME);

    // Entity Set Names
    public static final String ES_PRODUCTS_NAME = "Products";
    public static final String ES_ORDERS_NAME = "Orders";
    public static final String ES_PRODUCT_CATEGORIES_NAME = "ProductCategories";
    public static final String ES_SUPPLIERS_NAME = "Suppliers";
    public static final String ES_STOCKS_NAME = "Stocks";
    public static final String ES_CUSTOMERS_NAME = "Customers";
    public static final String ES_ROLES_NAME = "Roles";
    public static final String ES_LOCATIONS_NAME = "Locations";
    public static final String ES_REVENUES_NAME = "Revenues";
    public static final String ES_ORDER_DETAILS_NAME = "OrderDetails";
    public static final String ES_ADDRESS_NAME = "Addresses";

    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {

        // this method is called for one of the EntityTypes that are configured in the Schema
        if (entityTypeName.equals(ET_PRODUCT_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty name = new CsdlProperty().setName("name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty description = new CsdlProperty().setName("description").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty price = new CsdlProperty().setName("price").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
            CsdlProperty weight = new CsdlProperty().setName("weight").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
            CsdlProperty productCategories = new CsdlProperty().setName("productCategories").setCollection(true).setType(ET_PRODUCT_CATEGORY_NAME);
            CsdlProperty suppliers = new CsdlProperty().setName("suppliers").setCollection(true).setType(ET_SUPPLIER_NAME);
            CsdlProperty stocks = new CsdlProperty().setName("stocks").setCollection(true).setType(ET_STOCK_NAME);
            CsdlProperty orderDetails = new CsdlProperty().setName("orderDetails").setCollection(true).setType(ET_ORDER_DETAIL_NAME);

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_PRODUCT_NAME);
            entityType.setProperties(Arrays.asList(id, name, description, price, weight, productCategories, suppliers, stocks, orderDetails));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;

        } else if (entityTypeName.equals(ET_ORDER_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty shippedFrom = new CsdlProperty().setName("shippedFrom").setCollection(true).setType(ET_LOCATION_NAME);
            CsdlProperty customer = new CsdlProperty().setName("customer").setType(ET_CUSTOMER_NAME);
            CsdlProperty orderDetails = new CsdlProperty().setName("orderDetails").setCollection(true).setType(ET_ORDER_DETAIL_NAME);
            CsdlProperty address = new CsdlProperty().setName("address").setType(ET_ADDRESS_NAME);
            //TODO add timestamp here after it's working

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_ORDER_NAME);
            entityType.setProperties(Arrays.asList(id, shippedFrom, customer, orderDetails, address));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;

        } else if (entityTypeName.equals(ET_ORDER_DETAIL_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty product = new CsdlProperty().setName("product").setType(ET_PRODUCT_NAME);
            CsdlProperty order = new CsdlProperty().setName("order").setType(ET_ORDER_NAME);
            CsdlProperty quantity = new CsdlProperty().setName("quantity").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
            CsdlProperty location = new CsdlProperty().setName("location").setType(ET_LOCATION_NAME);

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_ORDER_DETAIL_NAME);
            entityType.setProperties(Arrays.asList(id, product, order, quantity, location));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;

        } else if (entityTypeName.equals(ET_PRODUCT_CATEGORY_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty products = new CsdlProperty().setName("products").setCollection(true).setType(ET_PRODUCT_NAME);
            CsdlProperty name = new CsdlProperty().setName("name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty description = new CsdlProperty().setName("description").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_PRODUCT_CATEGORY_NAME);
            entityType.setProperties(Arrays.asList(id, products, name, description));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;

        } else if (entityTypeName.equals(ET_STOCK_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty product = new CsdlProperty().setName("product").setType(ET_PRODUCT_NAME);
            CsdlProperty location = new CsdlProperty().setName("location").setType(ET_LOCATION_NAME);
            CsdlProperty quantity = new CsdlProperty().setName("quantity").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_STOCK_NAME);
            entityType.setProperties(Arrays.asList(id, product, location, quantity));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;

        } else if (entityTypeName.equals(ET_SUPPLIER_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty products = new CsdlProperty().setName("products").setCollection(true).setType(ET_PRODUCT_NAME);
            CsdlProperty name = new CsdlProperty().setName("name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_SUPPLIER_NAME);
            entityType.setProperties(Arrays.asList(id, products, name));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;

        } else if (entityTypeName.equals(ET_LOCATION_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty stocks = new CsdlProperty().setName("stocks").setCollection(true).setType(ET_STOCK_NAME);
            CsdlProperty orderDetails = new CsdlProperty().setName("orderDetails").setCollection(true).setType(ET_ORDER_DETAIL_NAME);
            CsdlProperty orders = new CsdlProperty().setName("orders").setCollection(true).setType(ET_ORDER_NAME);
            CsdlProperty name = new CsdlProperty().setName("name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty address = new CsdlProperty().setName("address").setType(ET_ADDRESS_NAME);
            CsdlProperty revenues = new CsdlProperty().setName("revenues").setCollection(true).setType(ET_REVENUE_NAME);

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_LOCATION_NAME);
            entityType.setProperties(Arrays.asList(id, stocks, orderDetails, orders, name, address, revenues));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;

        } else if (entityTypeName.equals(ET_REVENUE_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty location = new CsdlProperty().setName("location").setType(ET_LOCATION_NAME);
            //TODO add timestamp here
            CsdlProperty sum = new CsdlProperty().setName("sum").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_REVENUE_NAME);
            entityType.setProperties(Arrays.asList(id, location, sum));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;

        } else if (entityTypeName.equals(ET_CUSTOMER_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty orders = new CsdlProperty().setName("orders").setCollection(true).setType(ET_ORDER_NAME);
            CsdlProperty roles = new CsdlProperty().setName("roles").setCollection(true).setType(ET_ROLE_NAME);
            CsdlProperty firstName = new CsdlProperty().setName("firstName").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty lastName = new CsdlProperty().setName("lastName").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty username = new CsdlProperty().setName("username").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty password = new CsdlProperty().setName("password").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_CUSTOMER_NAME);
            entityType.setProperties(Arrays.asList(id, orders, roles, firstName, lastName, username, password));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;

        } else if (entityTypeName.equals(ET_ROLE_FQN)) {

            //create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty customers = new CsdlProperty().setName("customers").setCollection(true).setType(ET_CUSTOMER_NAME);
            CsdlProperty name = new CsdlProperty().setName("name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            // create CsdlPropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_ROLE_NAME);
            entityType.setProperties(Arrays.asList(id, customers, name));
            entityType.setKey(Collections.singletonList(propertyRef));

            return entityType;

        } else if (entityTypeName.equals(ET_ADDRESS_FQN)) {

            //create EntityType properties
            CsdlProperty country = new CsdlProperty().setName("country").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty city = new CsdlProperty().setName("city").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty county = new CsdlProperty().setName("county").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty streetAddress = new CsdlProperty().setName("streetAddress").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            // configure EntityType
            CsdlEntityType entityType = new CsdlEntityType();
            entityType.setName(ET_ADDRESS_NAME);
            entityType.setProperties(Arrays.asList(country, city, county, streetAddress));

            return entityType;

        }

        return null;
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {

        if (entityContainer.equals(CONTAINER)) {
            switch (entitySetName) {
                case ES_PRODUCTS_NAME: {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_PRODUCTS_NAME);
                    entitySet.setType(ET_PRODUCT_FQN);

                    return entitySet;
                }
                case ES_ORDERS_NAME: {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_ORDERS_NAME);
                    entitySet.setType(ET_ORDER_FQN);

                    return entitySet;
                }
                case ES_ORDER_DETAILS_NAME: {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_ORDER_DETAILS_NAME);
                    entitySet.setType(ET_ORDER_DETAIL_FQN);

                    return entitySet;
                }
                case ES_CUSTOMERS_NAME: {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_CUSTOMERS_NAME);
                    entitySet.setType(ET_CUSTOMER_FQN);

                    return entitySet;
                }
                case ES_PRODUCT_CATEGORIES_NAME: {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_PRODUCT_CATEGORIES_NAME);
                    entitySet.setType(ET_PRODUCT_CATEGORY_FQN);

                    return entitySet;
                }
                case ES_LOCATIONS_NAME: {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_LOCATIONS_NAME);
                    entitySet.setType(ET_LOCATION_FQN);

                    return entitySet;
                }
                case ES_REVENUES_NAME: {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_REVENUES_NAME);
                    entitySet.setType(ET_REVENUE_FQN);

                    return entitySet;
                }
                case ES_ROLES_NAME: {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_ROLES_NAME);
                    entitySet.setType(ET_ROLE_FQN);

                    return entitySet;
                }
                case ES_STOCKS_NAME: {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_STOCKS_NAME);
                    entitySet.setType(ET_STOCK_FQN);

                    return entitySet;
                }
                case ES_SUPPLIERS_NAME: {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_SUPPLIERS_NAME);
                    entitySet.setType(ET_SUPPLIER_FQN);

                    return entitySet;
                }
                case ES_ADDRESS_NAME: {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_ADDRESS_NAME);
                    entitySet.setType(ET_ADDRESS_FQN);

                    return entitySet;
                }
            }

        }

        return null;
    }

    @Override
    public CsdlEntityContainer getEntityContainer() {
        // create EntitySets
        List<CsdlEntitySet> entitySets = new ArrayList<>();
        entitySets.add(getEntitySet(CONTAINER, ES_PRODUCTS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_CUSTOMERS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_LOCATIONS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_ORDERS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_ORDER_DETAILS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_PRODUCT_CATEGORIES_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_REVENUES_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_ROLES_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_STOCKS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_SUPPLIERS_NAME));

        // create EntityContainer
        CsdlEntityContainer entityContainer = new CsdlEntityContainer();
        entityContainer.setName(CONTAINER_NAME);
        entityContainer.setEntitySets(entitySets);

        return entityContainer;
    }

    @Override
    public List<CsdlSchema> getSchemas() {
        // create Schema
        CsdlSchema schema = new CsdlSchema();
        schema.setNamespace(NAMESPACE);

        // add EntityTypes
        List<CsdlEntityType> entityTypes = new ArrayList<>();
        entityTypes.add(getEntityType(ET_PRODUCT_FQN));
        entityTypes.add(getEntityType(ET_CUSTOMER_FQN));
        entityTypes.add(getEntityType(ET_LOCATION_FQN));
        entityTypes.add(getEntityType(ET_ORDER_FQN));
        entityTypes.add(getEntityType(ET_ORDER_DETAIL_FQN));
        entityTypes.add(getEntityType(ET_PRODUCT_CATEGORY_FQN));
        entityTypes.add(getEntityType(ET_REVENUE_FQN));
        entityTypes.add(getEntityType(ET_ROLE_FQN));
        entityTypes.add(getEntityType(ET_STOCK_FQN));
        entityTypes.add(getEntityType(ET_SUPPLIER_FQN));
        schema.setEntityTypes(entityTypes);

        // add EntityContainer
        schema.setEntityContainer(getEntityContainer());

        // finally
        List<CsdlSchema> schemas = new ArrayList<>();
        schemas.add(schema);

        return schemas;
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
}
