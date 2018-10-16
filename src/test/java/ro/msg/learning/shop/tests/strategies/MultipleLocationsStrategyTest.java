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
import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;
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

        if (!product1.isPresent() && !product2.isPresent()) {
            throw new MySuperException("Products chosen problem!", "They don't exist in the db!");
        }

        OrderDetail orderDetail1 = new OrderDetail(1111, product1.get(), null, 100, null);
        OrderDetail orderDetail2 = new OrderDetail(2222, product2.get(), null, 100, null);


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

        Assert.assertEquals("Location for first wrapper is wrong!", 200, firstStrategyWrapper.getLocation().getId().intValue());
        Assert.assertEquals("Location for second wrapper is wrong!", 200, secondStrategyWrapper.getLocation().getId().intValue());
        Assert.assertEquals("Product ID for first wrapper is wrong!", 57, firstStrategyWrapper.getProduct().getId().intValue());
        Assert.assertEquals("Product ID for second wrapper is wrong!", 64, secondStrategyWrapper.getProduct().getId().intValue());
        Assert.assertEquals("Quantity for first wrapper is wrong!", 100, firstStrategyWrapper.getQuantity().intValue());
        Assert.assertEquals("Quantity for second wrapper is wrong!", 100, secondStrategyWrapper.getQuantity().intValue());
    }

    @Test
    public void beerBreadWestRomaniaTest() {
        List<OrderDetail> orderDetailList = new ArrayList<>();

        Optional<Product> product1 = productRepository.findById(1);
        Optional<Product> product2 = productRepository.findById(2);

        if (!product1.isPresent() && !product2.isPresent()) {
            throw new MySuperException("Products chosen problem!", "They don't exist in the db!");
        }

        OrderDetail orderDetail1 = new OrderDetail(1012, product1.get(), null, 10, null);
        OrderDetail orderDetail2 = new OrderDetail(1013, product2.get(), null, 10, null);

        orderDetailList.add(orderDetail1);
        orderDetailList.add(orderDetail2);

        Address testAddress = Address.builder().streetAddress("Cluj")
            .city("Arad")
            .country("Romania")
            .county("Arad")
            .build();

        List<StrategyWrapper> strategyWrapperList = multipleLocationsStrategy.getStrategyResult(orderDetailList, testAddress);

        StrategyWrapper firstStrategyWrapper = strategyWrapperList.get(0);

        Assert.assertEquals("Location for first wrapper is wrong!", 2, firstStrategyWrapper.getLocation().getId().intValue());
        Assert.assertEquals("Location for second wrapper is wrong!", 8, firstStrategyWrapper.getQuantity().intValue());
        Assert.assertEquals("Product ID for first wrapper is wrong!", 1, firstStrategyWrapper.getProduct().getId().intValue());
    }

}