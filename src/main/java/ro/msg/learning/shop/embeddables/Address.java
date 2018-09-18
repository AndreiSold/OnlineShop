package ro.msg.learning.shop.embeddables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String Country;
    private String City;
    private String County;
    private String StreetAddress;
}
