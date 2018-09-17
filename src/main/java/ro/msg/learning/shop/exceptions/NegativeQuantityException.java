package ro.msg.learning.shop.exceptions;


public class NegativeQuantityException extends RuntimeException {
    public NegativeQuantityException(Integer quantity) {
        super("Quantity should be strictly positive. Given value was: " + quantity);
    }
}
