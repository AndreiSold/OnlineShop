package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.ProductDto;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.mappers.ProductMapper;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> getPurchasedProductsInCurrentMonth() {
        return productMapper.productListToProductDtoList(productRepository.selectProductsPurchasedFromCurrentMonth());
    }

    public Product getProductById(Integer id) {
        return productRepository.getById(id);
    }

    public List<Product> getProductsInRange(Double startPrice, Double endPrice) {
        return productRepository.getProductsInRange(startPrice, endPrice);
    }

    public List<Product> getProductsFromCategory(Integer categoryId) {
        return productRepository.getProductsFromCategory(categoryId);
    }

    public List<Product> getProductsInRangeFromCategory(Double startPrice, Double endPrice, Integer categoryId) {
        return productRepository.getProductsInRangeFromCategory(startPrice, endPrice, categoryId);
    }

    public List<Product> getProductsThatContainString(String string) {
        return productRepository.getProductsThatContainString(string);
    }

    public void saveProduct(Product product) {

        Optional<Product> existentProduct = productRepository.findByName(product.getName());
        if (! existentProduct.isPresent())
            productRepository.save(product);
    }

    public void deleteProductById(Integer productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
