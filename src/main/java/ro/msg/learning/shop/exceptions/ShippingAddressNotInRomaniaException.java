package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class ShippingAddressNotInRomaniaException extends MySuperException {

    public ShippingAddressNotInRomaniaException(String country, String details) {
        super("We only ship in Romania. Customer wanted order to be delivered in: " + country, details);
    }
}
