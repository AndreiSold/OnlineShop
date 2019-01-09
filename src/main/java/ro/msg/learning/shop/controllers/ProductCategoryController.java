package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.services.ProductCategoryService;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getCategoryNameById(@PathVariable Integer id){
        return productCategoryService.getCategoryNameById(id);
    }

}
