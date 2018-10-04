package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class UsernameAlreadyExistsException extends MySuperException {

    public UsernameAlreadyExistsException(String details) {
        super("This username already exists! Please try another one!", details);
    }
}
