package ro.msg.learning.shop.dtos.distance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceResponseDto {

    private List<String> destination_addresses;
    private List<String> origin_addresses;
    private List<DistanceRowDto> rows;
    private String status;
}
