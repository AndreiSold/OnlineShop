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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailMapperTest {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Test
    public void emptyListAsParameterFromOrderDetailDtoListTest() {
        Assert.assertEquals(Collections.emptyList(), orderDetailMapper.orderDetailDtoListToOrderDetailList(new ArrayList<>()));
    }

    @Test(expected = NullPointerException.class)
    public void nullListAsParameterFromOrderDetailDtoListTest() {
        orderDetailMapper.orderDetailDtoListToOrderDetailList(null);
    }

    @Test
    public void oneItemInListFromOrderDetailDtoListTest() {
        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();
        OrderDetailDto toBeMappedObject = new OrderDetailDto(15, 200);
        orderDetailDtoList.add(toBeMappedObject);

        List<OrderDetail> orderDetailList = orderDetailMapper.orderDetailDtoListToOrderDetailList(orderDetailDtoList);

        OrderDetail createdObject = orderDetailList.get(0);

        Assert.assertEquals("Quantities are not equal!", toBeMappedObject.getQuantity().intValue(), createdObject.getQuantity().intValue());
        Assert.assertEquals("Product IDs are not equal!", toBeMappedObject.getProductId().intValue(), createdObject.getProduct().getId().intValue());
    }

    @Test
    public void threeItemsInListFromOrderDetailDtoListTest() {
        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>(Arrays.asList(
            new OrderDetailDto(15, 200),
            new OrderDetailDto(30, 400),
            new OrderDetailDto(45, 600)
        ));

        List<OrderDetail> orderDetailList = orderDetailMapper.orderDetailDtoListToOrderDetailList(orderDetailDtoList);

        orderDetailList.stream().forEach(orderDetailObject -> {
            int position = orderDetailList.indexOf(orderDetailObject);

            OrderDetailDto correspondingOrderDetailDto = orderDetailDtoList.get(position);

            Assert.assertEquals("Quantities are not equal for the object with index: " + position, correspondingOrderDetailDto.getQuantity().intValue(), orderDetailObject.getQuantity().intValue());
            Assert.assertEquals("Product IDs are not equal for the object with index: " + position, correspondingOrderDetailDto.getProductId().intValue(), orderDetailObject.getProduct().getId().intValue());
        });
    }

    @Test
    public void emptyListAsParameterFromStrategyWrapperListTest() {
        Assert.assertEquals(Collections.emptyList(), orderDetailMapper.strategyWrapperListToOrderDetailList(new ArrayList<>(), new Order()));
    }

    @Test(expected = NullPointerException.class)
    public void nullListAsParameterFromStrategyWrapperListTest() {
        orderDetailMapper.strategyWrapperListToOrderDetailList(null, null);
    }

    @Test
    public void oneItemInListFromStrategyWrapperListTest() {
        List<StrategyWrapper> strategyWrapperList = new ArrayList<>();

        StrategyWrapper toBeMappedObject = new StrategyWrapper(new Location(12, null, null, null, "Locatie", null),
            new Product(5, "Produs", "DescriereBlana", 500D, 1000D, null, null, null, null),
            200);

        strategyWrapperList.add(toBeMappedObject);

        List<OrderDetail> orderDetailList = orderDetailMapper.strategyWrapperListToOrderDetailList(strategyWrapperList, new Order());

        OrderDetail createdObject = orderDetailList.get(0);

        Assert.assertEquals("Quantities are not equal!", toBeMappedObject.getQuantity().intValue(), createdObject.getQuantity().intValue());
        Assert.assertEquals("Product IDs are not equal!", toBeMappedObject.getProduct().getId().intValue(), createdObject.getProduct().getId().intValue());
        Assert.assertEquals("Descriptions are not equal!", toBeMappedObject.getProduct().getDescription(), createdObject.getProduct().getDescription());
        Assert.assertEquals("Names are not equal!", toBeMappedObject.getProduct().getName(), createdObject.getProduct().getName());
        Assert.assertEquals("Prices are not equal!", toBeMappedObject.getProduct().getPrice(), createdObject.getProduct().getPrice());
        Assert.assertEquals("Weights are not equal!", toBeMappedObject.getProduct().getWeight(), createdObject.getProduct().getWeight());
    }

    @Test
    public void threeItemsInListFromStrategyWrapperListTest() {
        List<StrategyWrapper> strategyWrapperList = new ArrayList<>(Arrays.asList(
            new StrategyWrapper(new Location(12, null, null, null, "Locatie", null),
                new Product(5, "Produs", "DescriereBlana", 500D, 1000D, null, null, null, null), 200),
            new StrategyWrapper(new Location(20, null, null, null, "Locatie2", null),
                new Product(15, "Produs2", "DescriereBlana2", 502D, 1002D, null, null, null, null), 300),
            new StrategyWrapper(new Location(42, null, null, null, "Locatie3", null),
                new Product(25, "Produs", "DescriereBlana", 10001D, 5D, null, null, null, null), 400)
        ));

        List<OrderDetail> orderDetailList = orderDetailMapper.strategyWrapperListToOrderDetailList(strategyWrapperList, new Order());

        orderDetailList.stream().forEach(orderDetailObject -> {
            int position = orderDetailList.indexOf(orderDetailObject);

            StrategyWrapper correspondingStrategyWrapperObject = strategyWrapperList.get(position);

            Assert.assertEquals("Quantities are not equal for the object with index: " + position, correspondingStrategyWrapperObject.getQuantity().intValue(), orderDetailObject.getQuantity().intValue());
            Assert.assertEquals("Product IDs are not equal for the object with index: " + position, correspondingStrategyWrapperObject.getProduct().getId().intValue(), orderDetailObject.getProduct().getId().intValue());
            Assert.assertEquals("Descriptions are not equal for the object with index: " + position, correspondingStrategyWrapperObject.getProduct().getDescription(), orderDetailObject.getProduct().getDescription());
            Assert.assertEquals("Names are not equal for the object with index: " + position, correspondingStrategyWrapperObject.getProduct().getName(), orderDetailObject.getProduct().getName());
            Assert.assertEquals("Prices are not equal for the object with index: " + position, correspondingStrategyWrapperObject.getProduct().getPrice(), orderDetailObject.getProduct().getPrice(), 0.1);
            Assert.assertEquals("Weights are not equal for the object with index: " + position, correspondingStrategyWrapperObject.getProduct().getWeight(), orderDetailObject.getProduct().getWeight(), 0.1);
        });
    }
}