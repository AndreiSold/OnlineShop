package ro.msg.learning.shop.tests.mappers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailMapperTest {

    @Autowired
    private OrderDetailMapper orderDetailMapperMock;

    @Test
    public void emptyListAsParameterFromOrderDetailDtoListTest() {
        Assert.assertEquals(Collections.emptyList(), orderDetailMapperMock.orderDetailDtoListToOrderDetailList(new ArrayList<>()));
    }

    @Test(expected = NullPointerException.class)
    public void nullListAsParameterFromOrderDetailDtoListTest() {
        orderDetailMapperMock.orderDetailDtoListToOrderDetailList(null);
        Assert.assertFalse(true);
    }

    @Test
    public void oneItemInListFromOrderDetailDtoListTest() {
        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();
        OrderDetailDto toBeMappedObject = new OrderDetailDto(15, 200);
        orderDetailDtoList.add(toBeMappedObject);

        List<OrderDetail> orderDetailList = orderDetailMapperMock.orderDetailDtoListToOrderDetailList(orderDetailDtoList);

        OrderDetail createdObject = orderDetailList.get(0);

        Assert.assertEquals("Quantities are not equal!", toBeMappedObject.getQuantity().intValue(), createdObject.getQuantity().intValue());
        Assert.assertEquals("Product IDs are not equal!", toBeMappedObject.getProductId().intValue(), createdObject.getProduct().getId().intValue());
    }

    @Test
    public void threeItemsInListFromOrderDetailDtoListTest() {
        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();

        OrderDetailDto firstObjectToBeMapped = new OrderDetailDto(15, 200);
        OrderDetailDto secondObjectToBeMapped = new OrderDetailDto(30, 400);
        OrderDetailDto thirdObjectToBeMapped = new OrderDetailDto(45, 600);

        orderDetailDtoList.add(firstObjectToBeMapped);
        orderDetailDtoList.add(secondObjectToBeMapped);
        orderDetailDtoList.add(thirdObjectToBeMapped);

        List<OrderDetail> orderDetailList = orderDetailMapperMock.orderDetailDtoListToOrderDetailList(orderDetailDtoList);

        OrderDetail firstObjectCreated = orderDetailList.get(0);
        OrderDetail secondObjectCreated = orderDetailList.get(1);
        OrderDetail thirdObjectCreated = orderDetailList.get(2);

        Assert.assertEquals("Quantities are not equal for the first object!", firstObjectToBeMapped.getQuantity().intValue(), firstObjectCreated.getQuantity().intValue());
        Assert.assertEquals("Product IDs are not equal for the first object!", firstObjectToBeMapped.getProductId().intValue(), firstObjectCreated.getProduct().getId().intValue());

        Assert.assertEquals("Quantities are not equal for the second object!", secondObjectToBeMapped.getQuantity().intValue(), secondObjectCreated.getQuantity().intValue());
        Assert.assertEquals("Product IDs are not equal for the second object!", secondObjectToBeMapped.getProductId().intValue(), secondObjectCreated.getProduct().getId().intValue());

        Assert.assertEquals("Quantities are not equal for the third object!", thirdObjectToBeMapped.getQuantity().intValue(), thirdObjectCreated.getQuantity().intValue());
        Assert.assertEquals("Product IDs are not equal for the third object!", thirdObjectToBeMapped.getProductId().intValue(), thirdObjectCreated.getProduct().getId().intValue());
    }

    @Test
    public void emptyListAsParameterFromStrategyWrapperListTest() {
        Assert.assertEquals(Collections.emptyList(), orderDetailMapperMock.strategyWrapperListToOrderDetailList(new ArrayList<>(), new Order()));
    }

    @Test(expected = NullPointerException.class)
    public void nullListAsParameterFromStrategyWrapperListTest() {
        orderDetailMapperMock.strategyWrapperListToOrderDetailList(null, null);
        Assert.assertFalse(true);
    }

    @Test
    public void oneItemInListFromStrategyWrapperListTest() {
        List<StrategyWrapper> strategyWrapperList = new ArrayList<>();

        StrategyWrapper toBeMappedObject = new StrategyWrapper(new Location(12, null, null, "Locatie", null),
            new Product(5, "Produs", "DescriereBlana", 500D, 1000D, null, null, null, null),
            200);

        strategyWrapperList.add(toBeMappedObject);

        List<OrderDetail> orderDetailList = orderDetailMapperMock.strategyWrapperListToOrderDetailList(strategyWrapperList, new Order());

        OrderDetail createdObject = orderDetailList.get(0);

        Assert.assertEquals("Quantities are not equal!", toBeMappedObject.getQuantity().intValue(), createdObject.getQuantity().intValue());
        Assert.assertEquals("Product IDs are not equal!", toBeMappedObject.getProduct().getId().intValue(), createdObject.getProduct().getId().intValue());
        Assert.assertEquals("Descriptions are not equal!", toBeMappedObject.getProduct().getDescription(), createdObject.getProduct().getDescription());
        Assert.assertEquals("Names are not equal!", toBeMappedObject.getProduct().getName(), createdObject.getProduct().getName());
        Assert.assertEquals("Prices are not equal!", toBeMappedObject.getProduct().getPrice(), createdObject.getProduct().getPrice(), 0.1);
        Assert.assertEquals("Weights are not equal!", toBeMappedObject.getProduct().getWeight(), createdObject.getProduct().getWeight(), 0.1);
    }

    @Test
    public void threeItemsInListFromStrategyWrapperListTest() {
        List<StrategyWrapper> strategyWrapperList = new ArrayList<>();

        StrategyWrapper firstToBeMappedObject = new StrategyWrapper(new Location(12, null, null, "Locatie", null),
            new Product(5, "Produs", "DescriereBlana", 500D, 1000D, null, null, null, null), 200);
        StrategyWrapper secondToBeMappedObject = new StrategyWrapper(new Location(20, null, null, "Locatie2", null),
            new Product(15, "Produs2", "DescriereBlana2", 502D, 1002D, null, null, null, null), 300);
        StrategyWrapper thirdToBeMappedObject = new StrategyWrapper(new Location(42, null, null, "Locatie3", null),
            new Product(25, "Produs", "DescriereBlana", 10001D, 5D, null, null, null, null), 400);

        strategyWrapperList.add(firstToBeMappedObject);
        strategyWrapperList.add(secondToBeMappedObject);
        strategyWrapperList.add(thirdToBeMappedObject);

        List<OrderDetail> orderDetailList = orderDetailMapperMock.strategyWrapperListToOrderDetailList(strategyWrapperList, new Order());

        OrderDetail firstCreatedObject = orderDetailList.get(0);
        OrderDetail secondCreatedObject = orderDetailList.get(1);
        OrderDetail thirdCreatedObject = orderDetailList.get(2);

        Assert.assertEquals("Quantities are not equal for the first object!", firstToBeMappedObject.getQuantity().intValue(), firstCreatedObject.getQuantity().intValue());
        Assert.assertEquals("Product IDs are not equal for the first object!", firstToBeMappedObject.getProduct().getId().intValue(), firstCreatedObject.getProduct().getId().intValue());
        Assert.assertEquals("Descriptions are not equal for the first object!", firstToBeMappedObject.getProduct().getDescription(), firstCreatedObject.getProduct().getDescription());
        Assert.assertEquals("Names are not equal for the first object!", firstToBeMappedObject.getProduct().getName(), firstCreatedObject.getProduct().getName());
        Assert.assertEquals("Prices are not equal for the first object!", firstToBeMappedObject.getProduct().getPrice(), firstCreatedObject.getProduct().getPrice(), 0.1);
        Assert.assertEquals("Weights are not equal for the first object!", firstToBeMappedObject.getProduct().getWeight(), firstCreatedObject.getProduct().getWeight(), 0.1);

        Assert.assertEquals("Quantities are not equal for the second object!", secondToBeMappedObject.getQuantity().intValue(), secondCreatedObject.getQuantity().intValue());
        Assert.assertEquals("Product IDs are not equal for the second object!", secondToBeMappedObject.getProduct().getId().intValue(), secondCreatedObject.getProduct().getId().intValue());
        Assert.assertEquals("Descriptions are not equal for the second object!", secondToBeMappedObject.getProduct().getDescription(), secondCreatedObject.getProduct().getDescription());
        Assert.assertEquals("Names are not equal for the second object!", secondToBeMappedObject.getProduct().getName(), secondCreatedObject.getProduct().getName());
        Assert.assertEquals("Prices are not equal for the second object!", secondToBeMappedObject.getProduct().getPrice(), secondCreatedObject.getProduct().getPrice(), 0.1);
        Assert.assertEquals("Weights are not equal for the second object!", secondToBeMappedObject.getProduct().getWeight(), secondCreatedObject.getProduct().getWeight(), 0.1);

        Assert.assertEquals("Quantities are not equal for the third object!", thirdToBeMappedObject.getQuantity().intValue(), thirdCreatedObject.getQuantity().intValue());
        Assert.assertEquals("Product IDs are not equal for the third object!", thirdToBeMappedObject.getProduct().getId().intValue(), thirdCreatedObject.getProduct().getId().intValue());
        Assert.assertEquals("Descriptions are not equal for the third object!", thirdToBeMappedObject.getProduct().getDescription(), thirdCreatedObject.getProduct().getDescription());
        Assert.assertEquals("Names are not equal for the third object!", thirdToBeMappedObject.getProduct().getName(), thirdCreatedObject.getProduct().getName());
        Assert.assertEquals("Prices are not equal for the third object!", thirdToBeMappedObject.getProduct().getPrice(), thirdCreatedObject.getProduct().getPrice(), 0.1);
        Assert.assertEquals("Weights are not equal for the third object!", thirdToBeMappedObject.getProduct().getWeight(), thirdCreatedObject.getProduct().getWeight(), 0.1);
    }
}