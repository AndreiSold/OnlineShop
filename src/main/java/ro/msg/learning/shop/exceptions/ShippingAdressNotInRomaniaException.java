package ro.msg.learning.shop.exceptions;

import lombok.Data;

@Data
public class ShippingAdressNotInRomaniaException extends RuntimeException {

    private String details;

    public ShippingAdressNotInRomaniaException(String country, String details) {
        super("We only ship in Romania. Customer wanted order to be delivered in: " + country);
        this.details = details;
    }
}
