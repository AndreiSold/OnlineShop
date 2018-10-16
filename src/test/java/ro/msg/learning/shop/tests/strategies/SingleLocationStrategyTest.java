package ro.msg.learning.shop.tests.strategies;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.OrderDetailsListEmptyException;
import ro.msg.learning.shop.exceptions.SuitableLocationNonexistentException;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SingleLocationStrategyTest {

    @Autowired
    private SingleLocationStrategy singleLocationStrategy;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private Flyway flyway;

    @After
    public void resetDB() {
        flyway.clean();
        flyway.migrate();
    }

    @Test(expected = NoSuchElementException.class)
    public void productIdNotInDatabaseTest() {
        Optional<Product> mockProductOptional = productRepository.findById(999999999);
        singleLocationStrategy.getStrategyResult(Collections.singletonList(new OrderDetail(null, mockProductOptional.get(), null, 999999, null)), null);
    }

    @Test(expected = SuitableLocationNonexistentException.class)
    public void noLocationWithWantedProductsTest() {

        Optional<Product> mockProductOptional = productRepository.findById(15);

        if (mockProductOptional.isPresent()) {
            singleLocationStrategy.getStrategyResult(Collections.singletonList(new OrderDetail(null, mockProductOptional.get(), null, 999999999, null)), null);
        } else {
            Assert.assertFalse("You chose a nonexistent product for this test! Bad bad dev", true);
        }
    }

    @Test(expected = OrderDetailsListEmptyException.class)
    public void emptyOrderDetailListTest() {
        Assert.assertEquals(Collections.emptyList(), singleLocationStrategy.getStrategyResult(new ArrayList<>(), null));
    }

    @Test(expected = NullPointerException.class)
    public void nullListAsParameterTest() {
        singleLocationStrategy.getStrategyResult(null, null);
    }

    @Test
    public void constraintsPassedTest() {

        List<OrderDetail> orderDetailList = new ArrayList<>();

        Optional<Product> product1 = productRepository.findById(83);
        Optional<Product> product2 = productRepository.findById(737);

        OrderDetail orderDetail1 = new OrderDetail(1111, product1.get(), null, 200, null);
        OrderDetail orderDetail2 = new OrderDetail(2222, product2.get(), null, 100, null);

        orderDetailList.add(orderDetail1);
        orderDetailList.add(orderDetail2);

        List<StrategyWrapper> strategyWrapperList = singleLocationStrategy.getStrategyResult(orderDetailList, null);

        StrategyWrapper firstStrategyWrapper = strategyWrapperList.get(0);
        StrategyWrapper secondStrategyWrapper = strategyWrapperList.get(1);

        Assert.assertEquals("Locations are not equal for the products to be bought!", firstStrategyWrapper.getLocation(), secondStrategyWrapper.getLocation());
        Assert.assertEquals("Quantities are not equal for the first object!", firstStrategyWrapper.getQuantity(), orderDetail1.getQuantity());
        Assert.assertEquals("Product IDs are not equal for the first object!", firstStrategyWrapper.getProduct().getId(), orderDetail1.getProduct().getId());
        Assert.assertEquals("Quantities are not equal for the second object!", secondStrategyWrapper.getQuantity(), orderDetail2.getQuantity());
        Assert.assertEquals("Product IDs are not equal for the second object!", secondStrategyWrapper.getProduct().getId(), orderDetail2.getProduct().getId());
    }

    @Test
    public void checkIfStockQuantitiesUpdateTest() {
        List<OrderDetail> orderDetailList = new ArrayList<>();

        Optional<Product> product1 = productRepository.findById(83);
        Optional<Product> product2 = productRepository.findById(737);

        OrderDetail orderDetail1 = new OrderDetail(1111, product1.get(), null, 200, null);
        OrderDetail orderDetail2 = new OrderDetail(2222, product2.get(), null, 100, null);

        orderDetailList.add(orderDetail1);
        orderDetailList.add(orderDetail2);

        Optional<Location> optionalLocation = locationRepository.findById(850);

        Stock firstStock = stockRepository.findByLocationEqualsAndProductEqualsAndQuantityGreaterThanEqual(
            optionalLocation.get(),
            product1.get(),
            200
        );

        Stock secondStock = stockRepository.findByLocationEqualsAndProductEqualsAndQuantityGreaterThanEqual(
            optionalLocation.get(),
            product2.get(),
            100
        );

        // no need for the result
        singleLocationStrategy.getStrategyResult(orderDetailList, null);

        Optional<Stock> newFirstStock = stockRepository.findById(firstStock.getId());
        Optional<Stock> newSecondStock = stockRepository.findById(secondStock.getId());

        Assert.assertEquals(firstStock.getQuantity() - 200, newFirstStock.get().getQuantity().intValue());
        Assert.assertEquals(secondStock.getQuantity() - 100, newSecondStock.get().getQuantity().intValue());
    }

}