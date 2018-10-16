package ro.msg.learning.shop.tests.strategies;

import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.embeddables.Address;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.strategies.MultipleLocationsStrategy;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultipleLocationsStrategyTest {

    @Autowired
    private MultipleLocationsStrategy multipleLocationsStrategy;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Flyway flyway;

    @After
    public void resetDB() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void constraintsPassedTest() {

        List<OrderDetail> orderDetailList = new ArrayList<>();

        Optional<Product> product1 = productRepository.findById(57);
        Optional<Product> product2 = productRepository.findById(64);

        OrderDetail orderDetail1 = new OrderDetail(1111, product1.get(), null, 100);
        OrderDetail orderDetail2 = new OrderDetail(2222, product2.get(), null, 100);

        orderDetailList.add(orderDetail1);
        orderDetailList.add(orderDetail2);

        Address testAddress = Address.builder().streetAddress("Cluj")
            .city("Arad")
            .country("Romania")
            .county("Arad")
            .build();

        List<StrategyWrapper> strategyWrapperList = multipleLocationsStrategy.getStrategyResult(orderDetailList, testAddress);

        StrategyWrapper firstStrategyWrapper = strategyWrapperList.get(0);
        StrategyWrapper secondStrategyWrapper = strategyWrapperList.get(1);

        Assert.assertEquals("Location for first wrapper is wrong!", firstStrategyWrapper.getLocation().getId().intValue(), 200);
        Assert.assertEquals("Location for second wrapper is wrong!", secondStrategyWrapper.getLocation().getId().intValue(), 200);
        Assert.assertEquals("Product ID for first wrapper is wrong!", firstStrategyWrapper.getProduct().getId().intValue(), 57);
        Assert.assertEquals("Product ID for second wrapper is wrong!", secondStrategyWrapper.getProduct().getId().intValue(), 64);
        Assert.assertEquals("Quantity for first wrapper is wrong!", firstStrategyWrapper.getQuantity().intValue(), 100);
        Assert.assertEquals("Quantity for second wrapper is wrong!", secondStrategyWrapper.getQuantity().intValue(), 100);
    }

    @Test
    public void beerBreadWestRomaniaTest() {
        List<OrderDetail> orderDetailList = new ArrayList<>();

        Optional<Product> product1 = productRepository.findById(1);
        Optional<Product> product2 = productRepository.findById(2);

        OrderDetail orderDetail1 = new OrderDetail(1012, product1.get(), null, 10);
        OrderDetail orderDetail2 = new OrderDetail(1013, product2.get(), null, 10);

        orderDetailList.add(orderDetail1);
        orderDetailList.add(orderDetail2);

        Address testAddress = Address.builder().streetAddress("Cluj")
            .city("Arad")
            .country("Romania")
            .county("Arad")
            .build();

        List<StrategyWrapper> strategyWrapperList = multipleLocationsStrategy.getStrategyResult(orderDetailList, testAddress);

        StrategyWrapper firstStrategyWrapper = strategyWrapperList.get(0);
        StrategyWrapper secondStrategyWrapper = strategyWrapperList.get(1);

        Assert.assertEquals("Location for first wrapper is wrong!", firstStrategyWrapper.getLocation().getId().intValue(), 200);
        Assert.assertEquals("Location for second wrapper is wrong!", secondStrategyWrapper.getLocation().getId().intValue(), 200);
        Assert.assertEquals("Product ID for first wrapper is wrong!", firstStrategyWrapper.getProduct().getId().intValue(), 57);
        Assert.assertEquals("Product ID for second wrapper is wrong!", secondStrategyWrapper.getProduct().getId().intValue(), 64);
        Assert.assertEquals("Quantity for first wrapper is wrong!", firstStrategyWrapper.getQuantity().intValue(), 100);
        Assert.assertEquals("Quantity for second wrapper is wrong!", secondStrategyWrapper.getQuantity().intValue(), 100);
    }

}