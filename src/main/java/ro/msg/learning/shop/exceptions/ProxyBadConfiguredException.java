package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class ProxyBadConfiguredException extends MySuperException {

    public ProxyBadConfiguredException(String details) {
        super("The settings for the proxy are wrongly formatted!", details);
    }
}
