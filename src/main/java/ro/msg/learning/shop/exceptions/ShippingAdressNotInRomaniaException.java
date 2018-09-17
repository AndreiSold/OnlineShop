package ro.msg.learning.shop.exceptions;

public class ShippingAdressNotInRomaniaException extends RuntimeException {

    public ShippingAdressNotInRomaniaException(String country) {
        super("We only ship in Romania. Customer wanted order to be delivered in: " + country);
    }
}
