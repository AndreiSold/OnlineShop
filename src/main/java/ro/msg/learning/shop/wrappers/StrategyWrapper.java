package ro.msg.learning.shop.wrappers;

import lombok.*;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyWrapper {
    private Location location;
    private Product product;
    private Integer quantity;
}
