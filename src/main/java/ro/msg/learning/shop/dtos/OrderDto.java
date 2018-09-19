package ro.msg.learning.shop.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ro.msg.learning.shop.embeddables.Address;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm")
    private LocalDateTime orderTimestamp;
    private Address address;
    private List<OrderDetailDto> orderDetails;
}
