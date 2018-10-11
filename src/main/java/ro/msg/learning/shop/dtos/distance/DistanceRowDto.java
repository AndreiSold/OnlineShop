package ro.msg.learning.shop.dtos.distance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceRowDto {

    private List<DistanceElement> elements;
}
