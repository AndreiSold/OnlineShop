package ro.msg.learning.shop.constrains;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.dtos.OrderDto;
import ro.msg.learning.shop.exceptions.NegativeQuantityException;
import ro.msg.learning.shop.exceptions.OrderTimestampInFutureException;
import ro.msg.learning.shop.exceptions.ShippingAdressNotInRomaniaException;

import java.time.LocalDateTime;

@Slf4j
@Component
public class OrderDtoConstrains {

    public void checkIfOrderDtoCorrectlyFormatted(OrderDto orderDto) {

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
    }

}
