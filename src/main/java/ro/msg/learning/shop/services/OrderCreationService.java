package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.constrains.OrderDtoConstrains;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.repositories.OrderDetailRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.strategies.SelectionStrategy;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCreationService {

    private final SelectionStrategy selectionStrategy;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderRepository orderRepository;
    private final OrderDtoConstrains orderDtoConstrains;

    public Order createOrder(OrderDto orderDto) {

        orderDtoConstrains.checkIfOrderDtoCorrectlyFormatted(orderDto);

        List<OrderDetail> orderDetailList = orderDetailMapper.orderDetailDtoListToOrderDetailList(orderDto.getOrderDetails());

        List<StrategyWrapper> strategyWrapperList = selectionStrategy.getStrategyResult(orderDetailList);

        Order finalOrder = Order.builder()
            .address(orderDto.getAddress())
            .timestamp(orderDto.getOrderTimestamp())
            .build();

        List<OrderDetail> orderDetailFinalList = orderDetailMapper.strategyWrapperListToOrderDetailList(strategyWrapperList, finalOrder);
        orderDetailRepository.saveAll(orderDetailFinalList);

        finalOrder.setOrderDetails(orderDetailFinalList);
        orderRepository.save(finalOrder);

        //TODO Customer still not tied to order here

        return finalOrder;
    }

}
