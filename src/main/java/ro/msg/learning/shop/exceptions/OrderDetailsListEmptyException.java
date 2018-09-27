package ro.msg.learning.shop.exceptions;

import lombok.Data;

@Data
public class OrderDetailsListEmptyException extends RuntimeException {

    private String details;

    public OrderDetailsListEmptyException(String details) {
        super("The order details list is empty!");
        this.details = details;
    }
}
