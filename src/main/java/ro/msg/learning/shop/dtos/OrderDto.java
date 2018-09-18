package ro.msg.learning.shop.dtos;

import lombok.Data;
import ro.msg.learning.shop.embeddables.Address;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDto {

    private LocalDate orderTimestamp;
    private Address address;
    private List<OrderDetailDto> orderDetails;
}
