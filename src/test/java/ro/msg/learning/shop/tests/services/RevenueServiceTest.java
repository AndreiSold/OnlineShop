package ro.msg.learning.shop.tests.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Revenue;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.OrderDetailRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.services.RevenueService;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
public class RevenueServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void createRevenueTest() {

        Product firstProduct = Product.builder().price(15D).build();
        Product secondProduct = Product.builder().price(25D).build();

        Order firstOrder = Order.builder().timestamp(LocalDateTime.now()).build();
        Order secondOrder = Order.builder().timestamp(LocalDateTime.now().minusDays(1)).build();

        OrderDetail firstOrderDetail = OrderDetail.builder().product(firstProduct).quantity(10).location(locationRepository.findById(1).get()).order(firstOrder).build();
        OrderDetail secondOrderDetail = OrderDetail.builder().product(secondProduct).quantity(15).location(locationRepository.findById(2).get()).order(firstOrder).build();
        OrderDetail thirdOrderDetail = OrderDetail.builder().product(firstProduct).quantity(20).location(locationRepository.findById(3).get()).order(secondOrder).build();

        productRepository.save(firstProduct);
        productRepository.save(secondProduct);
        orderRepository.save(firstOrder);
        orderRepository.save(secondOrder);
        orderDetailRepository.save(firstOrderDetail);
        orderDetailRepository.save(secondOrderDetail);
        orderDetailRepository.save(thirdOrderDetail);

        List<Revenue> createdRevenues = revenueService.createDailyRevenues();

        orderDetailRepository.deleteById(firstOrderDetail.getId());
        orderDetailRepository.deleteById(secondOrderDetail.getId());
        orderDetailRepository.deleteById(thirdOrderDetail.getId());
        productRepository.deleteById(firstProduct.getId());
        productRepository.deleteById(secondProduct.getId());
        orderRepository.deleteById(firstOrder.getId());
        orderRepository.deleteById(secondOrder.getId());

        Assert.assertEquals("Created revenues size is not correct!", 2, createdRevenues.size());
        Assert.assertEquals("The location id for the first created revenue is not correct!", 2, createdRevenues.get(0).getLocation().getId().intValue());
        Assert.assertEquals("The sum for the first created revenue is not correct!", 375D, createdRevenues.get(0).getSum(), 0.1);
        Assert.assertEquals("The location id for the second created revenue is not correct!", 1, createdRevenues.get(1).getLocation().getId().intValue());
        Assert.assertEquals("The sum for the second created revenue is not correct!", 150D, createdRevenues.get(1).getSum(), 0.1);
    }

}
