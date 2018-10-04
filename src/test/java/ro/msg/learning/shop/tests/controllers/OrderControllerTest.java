package ro.msg.learning.shop.tests.controllers;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.embeddables.Address;
import ro.msg.learning.shop.entities.Order;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

//import org.flywaydb.core.Flyway;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @LocalServerPort
    private int port;

    private String basePath;
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Flyway flyway;

    @After
    public void resetDB() {
        flyway.clean();
        flyway.migrate();
    }

    @Before
    public void init() {
        basePath = "http://localhost:" + port + "/orders/";
    }

    @Test
    public void createOrderNullOrderDtoTest() {

        OrderDto orderDto = null;

        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto);

        String finalPath = basePath + "/create-order/";

        try {

            restTemplate.postForEntity(finalPath, request, Order.class);
            Assert.fail("A location should not be found!");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getStatusCode());
        }

    }

    @Test
    public void createOrderTimeStampInFutureTest() {

        OrderDto orderDto = createPerfectOrderDto();
        orderDto.setOrderTimestamp(LocalDateTime.of(2020, Month.JANUARY, 1, 10, 10, 30));

        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto);

        String finalPath = basePath + "/create-order/";

        try {
            restTemplate.postForEntity(finalPath, request, Order.class);
            Assert.fail("A location should not be found!");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }

    }

    @Test
    public void createOrderNotRomaniaTest() {

        OrderDto orderDto = createPerfectOrderDto();
        orderDto.getAddress().setCountry("Hungary");

        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto);

        String finalPath = basePath + "/create-order/";

        try {
            restTemplate.postForEntity(finalPath, request, Order.class);
            Assert.fail("A location should not be found!");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }

    }

    @Test
    public void createOrderNegativeQuantityTest() {

        OrderDto orderDto = createPerfectOrderDto();
        orderDto.getOrderDetails().get(0).setQuantity(-200);

        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto);

        String finalPath = basePath + "/create-order/";

        try {
            restTemplate.postForEntity(finalPath, request, Order.class);
            Assert.fail("A location should not be found!");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }

    }

    @Test
    public void createOrderLocationNonexistentTest() {

        OrderDto orderDto = createPerfectOrderDto();
        orderDto.getOrderDetails().get(0).setProductId(15);

        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto);

        String finalPath = basePath + "/create-order/";

        try {
            restTemplate.postForEntity(finalPath, request, Order.class);
            Assert.fail("A location should not be found!");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }

    }

    @Test
    public void createOrderGoodParametersTest() {

        OrderDto orderDto = createPerfectOrderDto();

        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto);

        String finalPath = basePath + "/create-order/";

        ResponseEntity<Order> createdOrderEntity = restTemplate.postForEntity(finalPath, request, Order.class);
        Order createdOrder = createdOrderEntity.getBody();


        Assert.assertEquals("Romania", createdOrder.getAddress().getCountry());
        Assert.assertEquals("Arad", createdOrder.getAddress().getCity());
        Assert.assertEquals("Arad", createdOrder.getAddress().getCounty());
        Assert.assertEquals("Cluj", createdOrder.getAddress().getStreetAddress());
        Assert.assertEquals(200, createdOrder.getOrderDetails().get(0).getQuantity().intValue());
        Assert.assertEquals(16, createdOrder.getOrderDetails().get(0).getProduct().getId().intValue());
    }


    private OrderDto createPerfectOrderDto() {
        OrderDetailDto orderDetailDto = new OrderDetailDto(16, 200);

        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();
        orderDetailDtoList.add(orderDetailDto);

        OrderDto orderDto = new OrderDto();

        orderDto.setAddress(new Address("Romania", "Arad", "Arad", "Cluj"));
        orderDto.setOrderTimestamp(LocalDateTime.now());
        orderDto.setOrderDetails(orderDetailDtoList);

        return orderDto;
    }

}
