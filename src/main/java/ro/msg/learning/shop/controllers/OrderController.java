package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.services.OrderCreationService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderCreationService orderCreationService;

    @PostMapping("/create-order")
    public Order createOrder(@RequestBody OrderDto orderDto) {
        return orderCreationService.createOrder(orderDto);
    }
}
