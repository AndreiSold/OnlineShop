package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class SuitableLocationNonexistentException extends MySuperException {

    public SuitableLocationNonexistentException(String details) {
        super("There isn't any location having all the products the customer ordered!", details);
    }
}
