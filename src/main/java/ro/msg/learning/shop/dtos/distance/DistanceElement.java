package ro.msg.learning.shop.dtos.distance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceElement {

    private TextValueDto distance;
    private TextValueDto duration;
    private String status;
}
