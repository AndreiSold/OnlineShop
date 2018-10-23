package ro.msg.learning.shop.wrappers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyProductWrapper {

    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Double weight;

    private Double monthlyEarnings;
    private Integer monthlyQuantity;
}
