package ro.msg.learning.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.embeddables.Address;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.services.OrderCreationService;
import ro.msg.learning.shop.services.ProductService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Testing requests for now
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    List<Product> productList() {
        return productService.getAllProducts();
    }

    @GetMapping("/requestParam")
    String requestParamExample(@RequestParam(name = "field", defaultValue = "defaultField") String field) {
        return (field);
    }

    @GetMapping("pathVariable/{id}")
    int pathVariableExample(@PathVariable int id) {
        return id;
    }

    @PostMapping("post/{description}/{name}/{price}/{weight}")
    void postProduct(@PathVariable String description, @PathVariable String name, @PathVariable Double price, @PathVariable Double weight) {
        productService.saveNewProduct(description, name, price, weight);
    }

    @PostMapping("/post")
    Product postProduct2(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    //testing order creation service

    @Autowired
    private OrderCreationService orderCreationService;

    @PostMapping("/bla")
    public Order bla(){

        OrderDto orderDto=new OrderDto();
        OrderDetailDto orderDetailDto=new OrderDetailDto();
        //at -2 hits exception
        orderDetailDto.setQuantity(2);
        List<OrderDetailDto> orderDetailDtoList=new ArrayList<>();
        orderDetailDtoList.add(orderDetailDto);

        orderDto.setOrderDetails(orderDetailDtoList);
        //at 2020 12 12 hits exception
        orderDto.setOrderTimestamp(LocalDate.of(2017,12,12));

        Address address=new Address();
        //at other country that romania hits exception
        address.setCountry("Romania");
        orderDto.setAdress(address);

        return orderCreationService.createOrder(orderDto);
    }
}
