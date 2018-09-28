package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

import java.time.LocalDateTime;

public class OrderTimestampInFutureException extends MySuperException {

    public OrderTimestampInFutureException(LocalDateTime localDate, String details) {
        super("The order's timestamp is in future: " + localDate.toString(), details);
    }
}
