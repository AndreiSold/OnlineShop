package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class ResultedStockListEmptyException extends MySuperException {

    public ResultedStockListEmptyException(int locationId, String details) {
        super("Searched for stocks in location: " + locationId, details);
    }
}
