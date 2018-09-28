package ro.msg.learning.shop.dtos;

import lombok.*;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetailDto {
    private Integer productId;
    private Integer quantity;

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderDetailDto orderDetailDto = (OrderDetailDto) o;
        return Objects.equals(productId, orderDetailDto.productId) && Objects.equals(quantity, orderDetailDto.quantity);
    }
}
