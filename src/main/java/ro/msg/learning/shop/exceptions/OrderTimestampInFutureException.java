package ro.msg.learning.shop.exceptions;

import java.time.LocalDateTime;

public class OrderTimestampInFutureException extends RuntimeException {

    public OrderTimestampInFutureException(LocalDateTime localDate) {
        super("The order's timestamp is in future: " + localDate.toString());
    }
}
