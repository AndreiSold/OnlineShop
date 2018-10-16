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
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.strategies.ClosestSingleLocationStrategy;
import ro.msg.learning.shop.strategies.SelectionStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyWrapperMapperTest {

    @Autowired
    private StrategyWrapperMapper strategyWrapperMapper;
    @Autowired
    private SelectionStrategy selectionStrategy;
    @Autowired
    private ProductRepository productRepository;

    @Test(expected = NullPointerException.class)
    public void nullListParameterTest() {
        strategyWrapperMapper.createStrategyWrapperListAndUpdateStocks(new Location(), null);
    }

    @Test(expected = LocationPassedAsNullException.class)
    public void nullLocationParameterTest() {
        strategyWrapperMapper.createStrategyWrapperListAndUpdateStocks(null, new ArrayList<>());
    }

    @Test
    public void locationOkAndOneItemInListTest() {
        OrderDetail orderDetail = new OrderDetail(null, productRepository.findById(64).get(), null, 100, null);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        orderDetailList.add(orderDetail);

        Location location = new Location();

        List<StrategyWrapper> strategyWrapperList;
        if (selectionStrategy instanceof SingleLocationStrategy || selectionStrategy instanceof ClosestSingleLocationStrategy) {
            strategyWrapperList = strategyWrapperMapper.createStrategyWrapperListAndUpdateStocks(location, orderDetailList);
            strategyWrapperList.stream().forEach(strategyWrapper -> {
                Assert.assertEquals(orderDetail.getQuantity(), strategyWrapper.getQuantity());
                Assert.assertEquals(location, strategyWrapper.getLocation());
            });
        }

    }

    @Test
    public void locationOkAndThreeItemsInListTest() {

        List<OrderDetail> orderDetailList = new ArrayList<>(Arrays.asList(
            new OrderDetail(null, null, null, 100, null),
            new OrderDetail(null, null, null, 200, null),
            new OrderDetail(null, null, null, 300, null)
        ));

        Location location = new Location();

        List<StrategyWrapper> strategyWrapperList;
        if (selectionStrategy instanceof SingleLocationStrategy || selectionStrategy instanceof ClosestSingleLocationStrategy) {
            strategyWrapperList = strategyWrapperMapper.createStrategyWrapperListAndUpdateStocks(location, orderDetailList);

            strategyWrapperList.stream().forEach(strategyWrapper -> {
                int position = strategyWrapperList.indexOf(strategyWrapper);

                Assert.assertEquals(orderDetailList.get(position).getQuantity(), strategyWrapper.getQuantity());
                Assert.assertEquals(location, strategyWrapper.getLocation());
            });
        }

    }
}