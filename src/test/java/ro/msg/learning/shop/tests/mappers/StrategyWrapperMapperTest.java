package ro.msg.learning.shop.tests.mappers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.LocationPassedAsNullException;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyWrapperMapperTest {

    @Autowired
    private StrategyWrapperMapper strategyWrapperMapper;

    @Test(expected = NullPointerException.class)
    public void nullListParameterTest() {
        strategyWrapperMapper.createStrategyWrapperListFromLocationAndOrderDetails(new Location(), null);
    }

    @Test(expected = LocationPassedAsNullException.class)
    public void nullLocationParameterTest() {
        strategyWrapperMapper.createStrategyWrapperListFromLocationAndOrderDetails(null, new ArrayList<>());
    }

    @Test
    public void locationOkAndOneItemInListTest() {
        OrderDetail orderDetail = new OrderDetail(null, null, null, 100);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        orderDetailList.add(orderDetail);

        Location location = new Location();

        List<StrategyWrapper> strategyWrapperList = strategyWrapperMapper.createStrategyWrapperListFromLocationAndOrderDetails(location, orderDetailList);

        strategyWrapperList.stream().forEach(strategyWrapper -> {
            Assert.assertEquals(orderDetail.getQuantity(), strategyWrapper.getQuantity());
            Assert.assertEquals(location, strategyWrapper.getLocation());
        });
    }

    @Test
    public void locationOkAndThreeItemsInListTest() {

        List<OrderDetail> orderDetailList = new ArrayList<>(Arrays.asList(
            new OrderDetail(null, null, null, 100),
            new OrderDetail(null, null, null, 200),
            new OrderDetail(null, null, null, 300)
        ));

        Location location = new Location();

        List<StrategyWrapper> strategyWrapperList = strategyWrapperMapper.createStrategyWrapperListFromLocationAndOrderDetails(location, orderDetailList);

        strategyWrapperList.stream().forEach(strategyWrapper -> {
            int position = strategyWrapperList.indexOf(strategyWrapper);

            Assert.assertEquals(orderDetailList.get(position).getQuantity(), strategyWrapper.getQuantity());
            Assert.assertEquals(location, strategyWrapper.getLocation());
        });

    }
}