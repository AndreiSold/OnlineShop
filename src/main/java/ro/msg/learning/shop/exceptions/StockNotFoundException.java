package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class StockNotFoundException extends MySuperException {

    public StockNotFoundException(String details) {
        super("Given stock not found in the database", details);
    }
}
