package ro.msg.learning.shop.tests.controllers;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.embeddables.Address;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.enums.StrategyEnum;
import ro.msg.learning.shop.repositories.OrderRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
public class OrderControllerTest {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${initial-strategy:SINGLE_LOCATION}")
    private String strategy;

    @LocalServerPort
    private int port;

    private String basePath;

    private OAuth2RestTemplate oAuth2RestTemplate;

    @Before
    public void init() {
        basePath = "http://localhost:" + port + "/order";

        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setPassword("1234");
        resourceDetails.setUsername("admin");
        resourceDetails.setAccessTokenUri("http://localhost:" + port + "/oauth/token");
        resourceDetails.setClientId("my-trusted-client");
        resourceDetails.setScope(asList("read", "write"));
        resourceDetails.setClientSecret("secret");
        resourceDetails.setGrantType("password");

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
    }

    @Test(expected = HttpClientErrorException.class)
    public void createOrderTimeStampInFutureTest() {

        OrderDto orderDto = createPerfectOrderDto();
        orderDto.setOrderTimestamp(LocalDateTime.of(2020, Month.JANUARY, 1, 10, 10, 30));

        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto);

        String finalPath = basePath + "/create-order/";

        oAuth2RestTemplate.postForEntity(finalPath, request, Order.class);
    }

    @Test(expected = HttpClientErrorException.class)
    public void createOrderNegativeQuantityTest() {

        OrderDto orderDto = createPerfectOrderDto();
        orderDto.getOrderDetails().get(0).setQuantity(-200);

        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto);

        String finalPath = basePath + "/create-order/";

        oAuth2RestTemplate.postForEntity(finalPath, request, Order.class);
    }

    @Test(expected = HttpClientErrorException.class)
    public void createOrderLocationNonexistentTest() {

        OrderDto orderDto = createPerfectOrderDto();
        orderDto.getOrderDetails().get(0).setProductId(15);

        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto);

        String finalPath = basePath + "/create-order/";

        oAuth2RestTemplate.postForEntity(finalPath, request, Order.class);
    }

    @Test
    public void createOrderGoodParametersTest() {

        OrderDto orderDto = createPerfectOrderDto();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        httpHeaders.setAccept(Collections.singletonList(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)));

        HttpEntity<OrderDto> request = new HttpEntity<>(orderDto, httpHeaders);

        String finalPath = basePath + "/create-order/";
        ResponseEntity<Order> createdOrderEntity = oAuth2RestTemplate.postForEntity(finalPath, request, Order.class);

        Order createdOrder = createdOrderEntity.getBody();

        if (StrategyEnum.SINGLE_LOCATION.equals(StrategyEnum.valueOf(strategy))) {
            Assert.assertEquals("Romania", createdOrder.getAddress().getCountry());
            Assert.assertEquals("Arad", createdOrder.getAddress().getCity());
            Assert.assertEquals("Arad", createdOrder.getAddress().getCounty());
            Assert.assertEquals("Cluj", createdOrder.getAddress().getStreetAddress());
            Assert.assertEquals("admin", createdOrder.getCustomer().getUsername());
            Assert.assertEquals("admin", createdOrder.getCustomer().getFirstName());
            Assert.assertEquals("admin", createdOrder.getCustomer().getLastName());
        } else if (StrategyEnum.CLOSEST_SINGLE_LOCATION.equals(StrategyEnum.valueOf(strategy))) {
            Assert.assertEquals(HttpStatus.NOT_FOUND, createdOrderEntity.getStatusCode());
        }

        orderRepository.deleteById(createdOrder.getId());
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
