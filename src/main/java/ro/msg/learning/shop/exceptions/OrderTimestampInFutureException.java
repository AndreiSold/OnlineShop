package ro.msg.learning.shop.exceptions;

import java.time.LocalDate;

public class OrderTimestampInFutureException extends RuntimeException {

    public OrderTimestampInFutureException(LocalDate localDate) {
        super("The order's timestamp is in future: " + localDate.toString());
    }
}
