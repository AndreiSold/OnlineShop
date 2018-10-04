package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class CustomerIdNotFoundException extends MySuperException {

    public CustomerIdNotFoundException(String details) {
        super("The given customer id does not exist in the database!", details);
    }
}
