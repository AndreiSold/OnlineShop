package ro.msg.learning.shop.embeddables;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    private String country;
    private String city;
    private String county;
    private String streetAddress;
}
