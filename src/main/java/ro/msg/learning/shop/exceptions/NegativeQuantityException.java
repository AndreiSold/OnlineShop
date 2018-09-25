package ro.msg.learning.shop.exceptions;

import lombok.Data;

@Data
public class NegativeQuantityException extends RuntimeException {

    private String details;

    public NegativeQuantityException(Integer quantity, String details) {
        super("Quantity should be strictly positive. Given value was: " + quantity);
        this.details = details;
    }
}
