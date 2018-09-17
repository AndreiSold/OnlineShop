package ro.msg.learning.shop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.NegativeQuantityException;
import ro.msg.learning.shop.exceptions.OrderTimestampInFutureException;
import ro.msg.learning.shop.exceptions.ShippingAdressNotInRomaniaException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderCreationService {

    public Order createOrder(OrderDto orderDto) {

        //Checking if any product quantity is less than 1 + throwing an exception if so

//        orderDto.getOrderDetails().parallelStream()
//            .filter(dto -> dto.getQuantity() < 1)
//            .peek(dto -> log.error("Quantity should be strictly positive. Given OrderDetailsDto looks like: " + dto))
//            .map(OrderDetailDto::getQuantity)
//            .forEach(NegativeQuantityException::new);

        for (OrderDetailDto orderDetailDtoInstance : orderDto.getOrderDetails()) {
            Integer quantity = orderDetailDtoInstance.getQuantity();

            if (quantity < 1) {
                log.error("All quantities should be strictly positive. Encountered an OrderDetailDto with: " + orderDetailDtoInstance);
                throw new NegativeQuantityException(quantity);
            }
        }

        //Checking if the order timestamp is not in the future
        LocalDate timestamp = orderDto.getOrderTimestamp();

        if (timestamp.isAfter(LocalDate.now())) {
            log.error("The order's timestamp is in future. Encountered an OrderDto with: " + orderDto);
            throw new OrderTimestampInFutureException(timestamp);
        }

        //Checking if the shipping address is in Romania
        String country = orderDto.getAdress().getCountry();

        if (!(country.equals("Romania") || country.equals("ROMANIA"))) {
            log.error("We only ship in Romania. Encountered an OrderDto with: " + orderDto.getAdress());
            throw new ShippingAdressNotInRomaniaException(country);
        }

        //Here everything is fine so we create the Order instance and return it
        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (OrderDetailDto orderDetailDto : orderDto.getOrderDetails()) {
            OrderDetail orderDetail = OrderDetail.builder().id(orderDetailDto.getProductId())
                .quantity(orderDetailDto.getQuantity())
                .build();

            orderDetailList.add(orderDetail);
        }

        //TODO should I add a TIMESTAMP field for the Order entity?
        return Order.builder().address(orderDto.getAdress())
            .orderDetails(orderDetailList)
            .build();
    }

}
