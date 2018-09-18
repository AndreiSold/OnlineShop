package ro.msg.learning.shop.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;

@Data
@AllArgsConstructor
public class StrategyWrapper {

    private Location location;
    private Product product;
    private Integer quantity;
}
