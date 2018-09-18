package ro.msg.learning.shop.exceptions;

public class SuitableLocationInexistentException extends RuntimeException {

    public SuitableLocationInexistentException() {
        super("There isn't any location having all the products the customer ordered!");
    }
}
