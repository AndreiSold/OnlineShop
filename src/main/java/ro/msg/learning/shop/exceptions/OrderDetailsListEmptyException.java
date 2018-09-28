package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class OrderDetailsListEmptyException extends MySuperException {

    public OrderDetailsListEmptyException(String details) {
        super("The order details list is empty!", details);
    }
}
