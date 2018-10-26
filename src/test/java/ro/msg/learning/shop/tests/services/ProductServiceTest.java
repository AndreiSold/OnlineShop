package ro.msg.learning.shop.tests.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.ProductDto;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.OrderDetailRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.services.ProductService;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void productsPurchasedInLastMonthTest() {

        Product firstProduct = Product.builder().price(15D).build();
        Product secondProduct = Product.builder().price(25D).build();

        Order firstOrder = Order.builder().timestamp(LocalDateTime.now()).build();
        Order secondOrder = Order.builder().timestamp(LocalDateTime.now().minusMonths(1)).build();

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

        List<ProductDto> orderedProducts = productService.getPurchasedProductsInCurrentMonth();

        orderDetailRepository.deleteById(firstOrderDetail.getId());
        orderDetailRepository.deleteById(secondOrderDetail.getId());
        orderDetailRepository.deleteById(thirdOrderDetail.getId());
        productRepository.deleteById(firstProduct.getId());
        productRepository.deleteById(secondProduct.getId());
        orderRepository.deleteById(firstOrder.getId());
        orderRepository.deleteById(secondOrder.getId());

        Assert.assertEquals(2, orderedProducts.size());
    }


}
