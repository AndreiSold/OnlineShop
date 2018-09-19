package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.repositories.OrderDetailRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.wrappers.StrategyWrapper;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.NegativeQuantityException;
import ro.msg.learning.shop.exceptions.OrderTimestampInFutureException;
import ro.msg.learning.shop.exceptions.ShippingAdressNotInRomaniaException;
import ro.msg.learning.shop.strategies.SelectionStrategy;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCreationService {

    private final SelectionStrategy selectionStrategy;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderRepository orderRepository;

    public Order createOrder(OrderDto orderDto) {

        //Checking if any product quantity is less than 1 and throwing an exception if there is
        for (OrderDetailDto orderDetailDtoInstance : orderDto.getOrderDetails()) {
            Integer quantity = orderDetailDtoInstance.getQuantity();

            if (quantity < 1) {
                log.error("All quantities should be strictly positive. Encountered an OrderDetailDto with: " + orderDetailDtoInstance);
                throw new NegativeQuantityException(quantity);
            }
        }

        //Checking if the order timestamp is not in the future
        LocalDateTime timestamp = orderDto.getOrderTimestamp();

        if (timestamp.isAfter(LocalDateTime.now())) {
            log.error("The order's timestamp is in future. Encountered an OrderDto with: " + orderDto);
            throw new OrderTimestampInFutureException(timestamp);
        }

        //Checking if the shipping address is in Romania
        String country = orderDto.getAddress().getCountry();

        if (!("Romania".equals(country) || "ROMANIA".equals(country))) {
            log.error("We only ship in Romania. Encountered an OrderDto with: " + orderDto.getAddress());
            throw new ShippingAdressNotInRomaniaException(country);
        }

        //Here everything is fine so we create the Order instance and return it
        List<OrderDetail> orderDetailList = orderDetailMapper.orderDetailDtoListToOrderDetailList(orderDto.getOrderDetails());

        //Retrieving strategy wrapper list from currently selected strategy
        List<StrategyWrapper> strategyWrapperList = selectionStrategy.getStrategyResult(orderDetailList);

        //Here we must convert the strategy result into a orderDetail list
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
