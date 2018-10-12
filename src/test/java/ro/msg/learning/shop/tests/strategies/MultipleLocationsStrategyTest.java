package ro.msg.learning.shop.tests.strategies;

import org.flywaydb.core.Flyway;
import org.junit.After;
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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    @Test(expected = NotImplementedException.class)
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

//        StrategyWrapper firstStrategyWrapper = strategyWrapperList.get(0);
//        StrategyWrapper secondStrategyWrapper = strategyWrapperList.get(1);
//
//        Assert.assertEquals("Locations are not equal for the products to be bought!", firstStrategyWrapper.getLocation(), secondStrategyWrapper.getLocation());
//        Assert.assertEquals("Quantities are not equal for the first object!", firstStrategyWrapper.getQuantity(), orderDetail1.getQuantity());
//        Assert.assertEquals("Product IDs are not equal for the first object!", firstStrategyWrapper.getProduct().getId(), orderDetail1.getProduct().getId());
//        Assert.assertEquals("Quantities are not equal for the second object!", secondStrategyWrapper.getQuantity(), orderDetail2.getQuantity());
//        Assert.assertEquals("Product IDs are not equal for the second object!", secondStrategyWrapper.getProduct().getId(), orderDetail2.getProduct().getId());
//        Assert.assertEquals(secondStrategyWrapper.getLocation().getId().intValue(), 200);
    }

}