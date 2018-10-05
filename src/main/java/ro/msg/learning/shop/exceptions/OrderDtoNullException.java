package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class OrderDtoNullException extends MySuperException {

    public OrderDtoNullException(String details) {
        super("Given instance of orderDto is null!", details);
    }
}
