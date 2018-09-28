package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class NegativeQuantityException extends MySuperException {

    public NegativeQuantityException(int quantity, String details) {
        super("Quantity should be strictly positive. Given value was: " + quantity, details);
    }
}
