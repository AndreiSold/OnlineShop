package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.NegativeQuantityException;
import ro.msg.learning.shop.exceptions.OrderDtoNullException;
import ro.msg.learning.shop.exceptions.OrderTimestampInFutureException;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.OrderDetailRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.strategies.SelectionStrategy;
import ro.msg.learning.shop.wrappers.StrategyWrapper;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final SelectionStrategy selectionStrategy;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public Order createOrder(OrderDto orderDto, String customerUsername) {

        orderDto.setOrderTimestamp(LocalDateTime.now());

        checkIfOrderDtoCorrectlyFormatted(orderDto);

        List<OrderDetail> orderDetailList = orderDetailMapper.orderDetailDtoListToOrderDetailList(orderDto.getOrderDetails());

        List<StrategyWrapper> strategyWrapperList = selectionStrategy.getStrategyResult(orderDetailList, orderDto.getAddress());

        List<Location> shippedFrom = new ArrayList<>();
        for (StrategyWrapper strategyWrapper : strategyWrapperList) {
            if (!shippedFrom.contains(strategyWrapper.getLocation())) {
                shippedFrom.add(strategyWrapper.getLocation());
            }
        }

        Order finalOrder = Order.builder()
            .address(orderDto.getAddress())
            .timestamp(orderDto.getOrderTimestamp())
            .shippedFrom(shippedFrom)
            .build();

        List<OrderDetail> orderDetailFinalList = orderDetailMapper.strategyWrapperListToOrderDetailList(strategyWrapperList, finalOrder);
        orderDetailRepository.saveAll(orderDetailFinalList);

        finalOrder.setOrderDetails(orderDetailFinalList);
        orderRepository.save(finalOrder);

        finalOrder.setCustomer(customerRepository.findByUsername(customerUsername));

        return finalOrder;
    }

    private void checkIfOrderDtoCorrectlyFormatted(OrderDto orderDto) {

        if (orderDto == null) {
            log.error("Given orderDto is null!");
            throw new OrderDtoNullException("No more info needed!");
        }

        //Checking if any product quantity is less than 1 and throwing an exception if there is
        orderDto.getOrderDetails().forEach(orderDetailDtoInstance -> {
            Integer quantity = orderDetailDtoInstance.getQuantity();
            if (quantity < 1) {
                log.error("All quantities should be strictly positive. Encountered an OrderDetailDto with: ", orderDetailDtoInstance);
                throw new NegativeQuantityException(quantity, orderDetailDtoInstance.toString());
            }
        });

        //Checking if the order timestamp is not in the future
        LocalDateTime timestamp = orderDto.getOrderTimestamp();

        if (timestamp.isAfter(LocalDateTime.now())) {
            log.error("The order's timestamp is in future. Encountered an OrderDto with: ", orderDto);
            throw new OrderTimestampInFutureException(timestamp, orderDto.toString());
        }

    }

    public Order getOrderById(Integer orderId) {
        return orderRepository.getById(orderId);
    }

    public List<Order> getAllOrdersOfCustomerById(Integer customerId) {
        return orderRepository.getAllOrdersOfCustomerById(customerId);
    }

    public List<Order> getAllOrdersOfCustomerByUsername(String customerUsername) {
        return orderRepository.getAllOrdersOfCustomerByUsername(customerUsername);
    }
}
