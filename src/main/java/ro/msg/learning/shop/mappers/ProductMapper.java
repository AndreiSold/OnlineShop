package ro.msg.learning.shop.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.ProductDto;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.wrappers.MonthlyProductWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ProductRepository productRepository;

    public List<ProductDto> productListToProductDtoList(List<Product> productList) {

        return productList.parallelStream().map(product -> ProductDto.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .weight(product.getWeight())
            .build())
            .collect(Collectors.toList());
    }

    public List<MonthlyProductWrapper> productDtoListToProductWrapper(List<ProductDto> productDtoList) {

        List<MonthlyProductWrapper> monthlyProductWrapperList = new ArrayList<>();

        productDtoList.parallelStream().forEach(product -> {

            int monthlyQuantity = productRepository.getQuantitySoldInLastMonthByProductId(product.getId());

            monthlyProductWrapperList.add(MonthlyProductWrapper.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .weight(product.getWeight())
                .monthlyQuantity(monthlyQuantity)
                .monthlyEarnings(monthlyQuantity * product.getPrice())
                .build());
        });

        return monthlyProductWrapperList;
    }

}
