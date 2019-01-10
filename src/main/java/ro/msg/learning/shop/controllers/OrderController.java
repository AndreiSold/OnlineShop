package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.exceptions.FileTypeMismatchException;
import ro.msg.learning.shop.services.OrderService;
import ro.msg.learning.shop.utilities.CsvConverter;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CsvConverter csvConverter;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create-order/{customerUsername}")
    public Order createOrder(@RequestBody OrderDto orderDto, @PathVariable String customerUsername) {
        return orderService.createOrder(orderDto, customerUsername);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/totalSum/{orderId}")
    public Double getOrderTotalSumById(@PathVariable Integer orderId) {
        return orderService.getOrderTotalSumById(orderId);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Integer orderId) {
        return orderService.getOrderById(orderId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/customer-by-id/{customerId}")
    public List<Order> getAllOrdersOfCustomerById(@PathVariable Integer customerId) {
        return orderService.getAllOrdersOfCustomerById(customerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/customer-by-username/{customerUsername}")
    public List<Order> getAllOrdersOfCustomerByUsername(@PathVariable String customerUsername) {
        return orderService.getAllOrdersOfCustomerByUsername(customerUsername);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @SneakyThrows
    @PostMapping("/csv-from-file")
    public List<OrderDetailDto> readFromCsvFile(@RequestParam("file") MultipartFile file) {

        if (!file.getOriginalFilename().endsWith(".csv")) {
            throw new FileTypeMismatchException(file.getOriginalFilename(), "Use a .csv file!");
        }

        return csvConverter.fromCsv(OrderDetailDto.class, file.getInputStream());
    }
}
