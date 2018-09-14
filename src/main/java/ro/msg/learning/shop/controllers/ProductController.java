package ro.msg.learning.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.services.ProductService;

import java.math.BigInteger;
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
}
