package ro.msg.learning.shop.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderDetailMapper {

    private final ProductRepository productRepository;

    public List<OrderDetail> orderDetailDtoListToOrderDetailList(List<OrderDetailDto> orderDetailDtoList) {

        List<OrderDetail> orderDetailList = new ArrayList<>();

        orderDetailDtoList.stream().forEach(orderDetailDto -> {
            Optional<Product> currentProduct = productRepository.findById(orderDetailDto.getProductId());
            if (currentProduct.isPresent()) {
                Product existentProduct = currentProduct.get();
                OrderDetail orderDetail = OrderDetail.builder().product(existentProduct)
                    .quantity(orderDetailDto.getQuantity())
                    .build();

                orderDetailList.add(orderDetail);
            }
        });

        return orderDetailList;
    }

    public List<OrderDetail> strategyWrapperListToOrderDetailList(List<StrategyWrapper> strategyWrapperList, Order order) {

        List<OrderDetail> orderDetailList = new ArrayList<>();

        strategyWrapperList.stream().forEach(strategyWrapper -> {
            OrderDetail orderDetail = OrderDetail.builder()
                .quantity(strategyWrapper.getQuantity())
                .product(strategyWrapper.getProduct())
                .order(order)
                .build();
            orderDetailList.add(orderDetail);
        });

        return orderDetailList;
    }
}
