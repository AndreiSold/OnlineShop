package ro.msg.learning.shop.embeddables;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Address {
    private String Country;
    private String City;
    private String County;
    private String StreetAddress;
}
