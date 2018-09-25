package ro.msg.learning.shop.exceptions;

import lombok.Data;

@Data
public class SuitableLocationInexistentException extends RuntimeException {

    private String details;

    public SuitableLocationInexistentException(String details) {
        super("There isn't any location having all the products the customer ordered!");
        this.details = details;
    }
}
