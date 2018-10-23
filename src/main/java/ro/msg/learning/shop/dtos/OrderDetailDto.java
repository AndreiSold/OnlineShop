package ro.msg.learning.shop.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = {"productId", "quantity"})
public class OrderDetailDto {

    private Integer productId;
    private Integer quantity;
}
