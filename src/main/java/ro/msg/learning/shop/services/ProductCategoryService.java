package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.ProductCategory;
import ro.msg.learning.shop.repositories.ProductCategoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public String getCategoryNameById(Integer id) {

        Optional<ProductCategory> categoryOptional = productCategoryRepository.findById(id);

        return categoryOptional.map(ProductCategory::getName).orElse(null);
    }
}
