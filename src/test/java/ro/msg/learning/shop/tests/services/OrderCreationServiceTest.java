package ro.msg.learning.shop.tests.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.embeddables.Address;
import ro.msg.learning.shop.exceptions.NegativeQuantityException;
import ro.msg.learning.shop.exceptions.OrderTimestampInFutureException;
import ro.msg.learning.shop.exceptions.ShippingAddressNotInRomaniaException;
import ro.msg.learning.shop.services.OrderCreationService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderCreationServiceTest {

    @Autowired
    private OrderCreationService orderCreationService;

    @Test(expected = ShippingAddressNotInRomaniaException.class)
    public void countryNotRomaniaExceptionTest() {

        OrderDto orderDto = new OrderDto();
        orderDto.setAddress(new Address("Hungary", "Kosenom", "Titusko", "Sarma"));
        orderDto.setOrderTimestamp(LocalDateTime.now());
        orderDto.setOrderDetails(Collections.emptyList());

        orderCreationService.createOrder(orderDto);
    }

    @Test(expected = NegativeQuantityException.class)
    public void negativeQuantityExceptionTest() {

        OrderDto orderDto = new OrderDto();

        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();

        orderDetailDtoList.add(new OrderDetailDto(2, 10));
        orderDetailDtoList.add(new OrderDetailDto(5, -5));

        orderDto.setOrderDetails(orderDetailDtoList);
        orderDto.setOrderTimestamp(LocalDateTime.now());
        orderDto.setAddress(new Address("Romania", "Kosenom", "Titusko", "Sarma"));

        orderCreationService.createOrder(orderDto);
    }

    @Test(expected = OrderTimestampInFutureException.class)
    public void orderTimestampInFutureExceptionTest() {

        OrderDto orderDto = new OrderDto();

        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();

        orderDetailDtoList.add(new OrderDetailDto(2, 10));
        orderDetailDtoList.add(new OrderDetailDto(5, 15));

        orderDto.setOrderDetails(orderDetailDtoList);
        orderDto.setOrderTimestamp(LocalDateTime.of(2020, Month.JANUARY, 1, 10, 10, 30));
        orderDto.setAddress(new Address("Romania", "Kosenom", "Titusko", "Sarma"));

        orderCreationService.createOrder(orderDto);
    }

}