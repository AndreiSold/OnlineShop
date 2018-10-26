package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.ProductDto;
import ro.msg.learning.shop.mappers.ProductMapper;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> getPurchasedProductsInCurrentMonth() {
        return productMapper.productListToProductDtoList(productRepository.selectProductsPurchasedFromCurrentMonth());
    }
}
