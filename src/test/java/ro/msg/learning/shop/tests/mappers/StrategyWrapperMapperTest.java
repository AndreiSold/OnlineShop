package ro.msg.learning.shop.tests.mappers;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.LocationPassedAsNullException;
import ro.msg.learning.shop.mappers.StrategyWrapperMapper;
import ro.msg.learning.shop.repositories.LocationRepository;
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
@ActiveProfiles(profiles = "dev")
@Slf4j
public class StrategyWrapperMapperTest {

    @Autowired
    private StrategyWrapperMapper strategyWrapperMapper;
    @Autowired
    private SelectionStrategy selectionStrategy;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LocationRepository locationRepository;


    @Test(expected = NullPointerException.class)
    public void nullListParameterTest() {
        strategyWrapperMapper.createStrategyWrapperListForSingleLocationStrategy(new Location(), null);
    }

    @Test(expected = LocationPassedAsNullException.class)
    public void nullLocationParameterTest() {
        strategyWrapperMapper.createStrategyWrapperListForSingleLocationStrategy(null, new ArrayList<>());
    }

    @Test
    public void locationOkAndOneItemInListTest() {
        OrderDetail orderDetail = new OrderDetail(null, productRepository.findById(64).get(), null, 100, locationRepository.findById(200).get());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        orderDetailList.add(orderDetail);

        List<StrategyWrapper> strategyWrapperList;
        if (selectionStrategy instanceof SingleLocationStrategy || selectionStrategy instanceof ClosestSingleLocationStrategy) {
            strategyWrapperList = strategyWrapperMapper.createStrategyWrapperListForSingleLocationStrategy(orderDetail.getLocation(), orderDetailList);
            strategyWrapperList.stream().forEach(strategyWrapper -> {
                Assert.assertEquals(orderDetail.getQuantity(), strategyWrapper.getQuantity());
                Assert.assertEquals(orderDetail.getLocation(), strategyWrapper.getLocation());
            });
        }

    }

    @Test
    public void locationOkAndThreeItemsInListTest() {

        Location finalLocation = locationRepository.findById(8).get();

        List<OrderDetail> orderDetailList = new ArrayList<>(Arrays.asList(
            new OrderDetail(null, productRepository.findById(506).get(), null, 100, finalLocation),
            new OrderDetail(null, productRepository.findById(479).get(), null, 200, finalLocation),
            new OrderDetail(null, productRepository.findById(704).get(), null, 200, finalLocation)
        ));

        List<StrategyWrapper> strategyWrapperList;
        if (selectionStrategy instanceof SingleLocationStrategy || selectionStrategy instanceof ClosestSingleLocationStrategy) {
            strategyWrapperList = strategyWrapperMapper.createStrategyWrapperListForSingleLocationStrategy(finalLocation, orderDetailList);

            strategyWrapperList.stream().forEach(strategyWrapper -> {
                int position = strategyWrapperList.indexOf(strategyWrapper);

                Assert.assertEquals(orderDetailList.get(position).getQuantity(), strategyWrapper.getQuantity());
                Assert.assertEquals(finalLocation, strategyWrapper.getLocation());
            });
        }

    }
}