package ro.msg.learning.shop.wrappers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyWrapper {

    private Location location;
    private Product product;
    private Integer quantity;
}
