package ro.msg.learning.shop.odata;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.repositories.OrderDetailRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class Storage {

    private List<Entity> productList;
    private List<Entity> orderList;
    private List<Entity> orderDetailsList;

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public Storage(ProductRepository productRepository, OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        productList = new ArrayList<>();
        orderList = new ArrayList<>();
        initData();
    }

    /* PUBLIC FACADE */
    public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet) {
        // actually, this is only required if we have more than one Entity Sets
        if (edmEntitySet.getName().equals(CustomEdmProvider.ES_PRODUCTS_NAME)) {
            return getProducts();
        }
        if (edmEntitySet.getName().equals(CustomEdmProvider.ES_ORDERS_NAME)) {
            return getOrders();
        }
        if (edmEntitySet.getName().equals(CustomEdmProvider.ES_ORDERS_DETAILS_NAME)) {
            return getOrderDetails();
        }
        return null;
    }

    public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException {

        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        // actually, this is only required if we have more than one Entity Type
        if (edmEntityType.getName().equals(CustomEdmProvider.ET_PRODUCT_NAME)) {
            return getProduct(edmEntityType, keyParams);
        }
        if (edmEntityType.getName().equals(CustomEdmProvider.ET_ORDER_NAME)) {
            return getOrder(edmEntityType, keyParams);
        }
        if (edmEntityType.getName().equals(CustomEdmProvider.ET_ORDER_DETAILS_NAME)) {
            return getOrderDetail(edmEntityType, keyParams);
        }


        return null;
    }

    /*  INTERNAL */

    private EntityCollection getOrders() {
        EntityCollection retEntitySet = new EntityCollection();

        for (Entity orderEntity : this.orderList) {
            retEntitySet.getEntities().add(orderEntity);
        }

        return retEntitySet;
    }

    private EntityCollection getProducts() {
        EntityCollection retEntitySet = new EntityCollection();

        for (Entity productEntity : this.productList) {
            retEntitySet.getEntities().add(productEntity);
        }

        return retEntitySet;
    }

    private EntityCollection getOrderDetails() {
        EntityCollection retEntitySet = new EntityCollection();

        for (Entity orderDetailEntity : this.orderDetailsList) {
            retEntitySet.getEntities().add(orderDetailEntity);
        }

        return retEntitySet;
    }

    private Entity getProduct(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException {

        // the list of entities at runtime
        EntityCollection entitySet = getProducts();

        /*  generic approach  to find the requested entity */
        Entity requestedEntity = CustomUtil.findEntity(edmEntityType, entitySet, keyParams);

        if (requestedEntity == null) {
            // this variable is null if our data doesn't contain an entity for the requested key
            // Throw suitable exception
            throw new ODataApplicationException("Entity for requested key doesn't exist",
                HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        return requestedEntity;
    }


    private Entity getOrder(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException {

        // the list of entities at runtime
        EntityCollection entitySet = getOrders();

        /*  generic approach  to find the requested entity */
        Entity requestedEntity = CustomUtil.findEntity(edmEntityType, entitySet, keyParams);

        if (requestedEntity == null) {
            // this variable is null if our data doesn't contain an entity for the requested key
            // Throw suitable exception
            throw new ODataApplicationException("Entity for requested key doesn't exist",
                HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        return requestedEntity;
    }

    private Entity getOrderDetail(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException {

        // the list of entities at runtime
        EntityCollection entitySet = getOrderDetails();

        /*  generic approach  to find the requested entity */
        Entity requestedEntity = CustomUtil.findEntity(edmEntityType, entitySet, keyParams);

        if (requestedEntity == null) {
            // this variable is null if our data doesn't contain an entity for the requested key
            // Throw suitable exception
            throw new ODataApplicationException("Entity for requested key doesn't exist",
                HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        return requestedEntity;
    }

    /* HELPER */
    private void initData() {

        productList = productRepository.findAll().parallelStream().map(product -> {
            final Entity e1 = new Entity()
                .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, product.getId()))
                .addProperty(new Property(null, "Name", ValueType.PRIMITIVE, product.getName()))
                .addProperty(new Property(null, "Description", ValueType.PRIMITIVE, product.getDescription()))
                .addProperty(new Property(null, "OrderDetailsIds", ValueType.COLLECTION_PRIMITIVE, product.getOrderDetails().parallelStream().map(OrderDetail::getId).collect(Collectors.toList())));
            e1.setType(CustomEdmProvider.ET_PRODUCT_FQN.getFullQualifiedNameAsString());
            e1.setId(createId("Products", product.getId()));
            return e1;
        }).collect(Collectors.toList());

        orderList = orderRepository.findAll().parallelStream().map(order -> {
            final Entity e1 = new Entity()
                .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, order.getId()))
                .addProperty(new Property(null, "Address", ValueType.PRIMITIVE, order.getAddress()))
                .addProperty(new Property(null, "OrderDetailsIds", ValueType.COLLECTION_PRIMITIVE,
                    order.getOrderDetails().parallelStream().map(OrderDetail::getId).collect(Collectors.toList())));
            e1.setType(CustomEdmProvider.ET_ORDER_FQN.getFullQualifiedNameAsString());
            e1.setId(createId("Orders", order.getId()));
            return e1;
        }).collect(Collectors.toList());

        orderDetailsList = orderDetailRepository.findAll().parallelStream().map(orderDetail -> {
            final Entity e1 = new Entity()
                .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, orderDetail.getId()))
                .addProperty(new Property(null, "Quantity", ValueType.PRIMITIVE, orderDetail.getQuantity()))
                .addProperty(new Property(null, "ProductId", ValueType.ENTITY, orderDetail.getProduct().getId()))
                .addProperty(new Property(null, "OrderId", ValueType.ENTITY, orderDetail.getOrder().getId()));

            e1.setId(createId("OrderDetails", orderDetail.getId()));
            e1.setType(CustomEdmProvider.ET_ORDER_DETAILS_FQN.getFullQualifiedNameAsString());
            return e1;
        }).collect(Collectors.toList());

    }

    private URI createId(String entitySetName, Object id) {
        try {
            return new URI(entitySetName + "(" + String.valueOf(id) + ")");
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
        }
    }

    public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType) {
        EntityCollection collection = getRelatedEntityCollection(entity, relatedEntityType);
        if (collection.getEntities().isEmpty()) {
            return null;
        }
        return collection.getEntities().get(0);
    }

    public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType, List<UriParameter> keyPredicates) {

        EntityCollection relatedEntities = getRelatedEntityCollection(entity, relatedEntityType);
        return CustomUtil.findEntity(relatedEntityType, relatedEntities, keyPredicates);
    }

    public EntityCollection getRelatedEntityCollection(Entity sourceEntity, EdmEntityType targetEntityType) {
        EntityCollection navigationTargetEntityCollection = new EntityCollection();

        FullQualifiedName relatedEntityFqn = targetEntityType.getFullQualifiedName();
        String sourceEntityFqn = sourceEntity.getType();

        if (sourceEntityFqn.equals(CustomEdmProvider.ET_ORDER_DETAILS_FQN.getFullQualifiedNameAsString()) && relatedEntityFqn.equals(CustomEdmProvider.ET_PRODUCT_FQN)) {
            // relation OrderDetails->Product (result the product)
            navigationTargetEntityCollection.getEntities().add(productList.get((Integer) sourceEntity.getProperty("ProductId").getValue()));

        } else {
            if (sourceEntityFqn.equals(CustomEdmProvider.ET_PRODUCT_FQN.getFullQualifiedNameAsString())
                && relatedEntityFqn.equals(CustomEdmProvider.ET_ORDER_DETAILS_FQN)) {
                // relation Product->OrderDetails (result all OrderDetails)

                ((List<Integer>) sourceEntity.getProperty("OrderDetailsIds").getValue()).
                    parallelStream().forEach(id -> navigationTargetEntityCollection.getEntities().add(orderDetailsList.get(id)));

            } else {
                if (sourceEntityFqn.equals(CustomEdmProvider.ET_ORDER_DETAILS_FQN.getFullQualifiedNameAsString()) && relatedEntityFqn.equals(CustomEdmProvider.ET_ORDER_FQN)) {

                    navigationTargetEntityCollection.getEntities().add(orderList.get((Integer) sourceEntity.getProperty("OrderId").getValue()));
                } else {
                    if (sourceEntityFqn.equals(CustomEdmProvider.ET_ORDER_FQN.getFullQualifiedNameAsString())
                        && relatedEntityFqn.equals(CustomEdmProvider.ET_ORDER_DETAILS_FQN)) {

                        ((List<Integer>) sourceEntity.getProperty("OrderDetailsIds").getValue()).
                            parallelStream().forEach(id -> navigationTargetEntityCollection.getEntities().add(orderDetailsList.get(id)));

                    }
                }
            }
        }

        if (navigationTargetEntityCollection.getEntities().isEmpty()) {
            return null;
        }

        return navigationTargetEntityCollection;
    }

}
