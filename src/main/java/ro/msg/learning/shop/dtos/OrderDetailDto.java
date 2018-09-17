package ro.msg.learning.shop.dtos;

import lombok.Data;

@Data
public class OrderDetailDto {

    private Integer productId;
    private Integer quantity;
}
