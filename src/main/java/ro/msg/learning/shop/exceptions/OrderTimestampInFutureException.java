package ro.msg.learning.shop.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderTimestampInFutureException extends RuntimeException {

    private String details;

    public OrderTimestampInFutureException(LocalDateTime localDate, String details) {
        super("The order's timestamp is in future: " + localDate.toString());
        this.details = details;
    }
}
