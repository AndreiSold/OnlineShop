package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.services.ProductService;

import javax.transaction.Transactional;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/from/{startPrice}/to/{endPrice}")
    public List<Product> getProductsInRange(@PathVariable Double startPrice, @PathVariable Double endPrice) {
        return productService.getProductsInRange(startPrice, endPrice);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsFromCategory(@PathVariable Integer categoryId) {
        return productService.getProductsFromCategory(categoryId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/from/{startPrice}/to/{endPrice}/category/{categoryId}")
    public List<Product> getProductsInRangeFromCategory(@PathVariable Integer categoryId, @PathVariable Double startPrice, @PathVariable Double endPrice) {
        return productService.getProductsInRangeFromCategory(startPrice, endPrice, categoryId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/contains/{string}")
    public List<Product> getProductsThatContainString(@PathVariable String string) {
        return productService.getProductsThatContainString(string);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/from/{startPrice}/to/{endPrice}/contains/{string}")
    public List<Product> getProductsInRangeContainsString(@PathVariable Double startPrice, @PathVariable Double endPrice, @PathVariable String string) {
        List<Product> productsInRange = productService.getProductsInRange(startPrice, endPrice);
        List<Product> productsThatContainString = productService.getProductsThatContainString(string);

        List<Product> filteredProducts = productsInRange;
        filteredProducts.retainAll(productsThatContainString);

        return filteredProducts;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/category/{category}/contains/{string}")
    public List<Product> getProductsFromCategoryContainsString(@PathVariable Integer category, @PathVariable String string) {
        List<Product> productsFromCategory = productService.getProductsFromCategory(category);
        List<Product> productsThatContainString = productService.getProductsThatContainString(string);

        List<Product> filteredProducts = productsFromCategory;
        filteredProducts.retainAll(productsThatContainString);

        return filteredProducts;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/from/{startPrice}/to/{endPrice}/category/{categoryId}/contains/{string}")
    public List<Product> getProductsInRangeFromCategoryThatContainString(@PathVariable Double startPrice, @PathVariable Double endPrice, @PathVariable Integer categoryId, @PathVariable String string) {
        List<Product> productsInRange = productService.getProductsInRange(startPrice, endPrice);
        List<Product> productsFromCategory = productService.getProductsFromCategory(categoryId);
        List<Product> productsThatContainString = productService.getProductsThatContainString(string);

        List<Product> filteredProducts = productsInRange;
        filteredProducts.retainAll(productsFromCategory);
        filteredProducts.retainAll(productsThatContainString);

        return filteredProducts;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public Integer addProduct(@RequestBody Product product) {
        productService.saveProduct(product);

        return product.getId();
    }

    @Transactional
    @Modifying
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add-picture/{productId}")
    public void addPicture(@RequestBody String imageUrl, @PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
        product.setImage(imageUrl);

        productService.saveProduct(product);
    }

    @Transactional
    @Modifying
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete/{productId}")
    public void deleteProductById(@PathVariable Integer productId) {
        productService.deleteProductById(productId);
    }

}
