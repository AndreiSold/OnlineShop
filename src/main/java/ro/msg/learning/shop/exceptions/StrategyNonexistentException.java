package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class StrategyNonexistentException extends MySuperException {

    public StrategyNonexistentException(String strategyName, String details) {
        super("Given selection strategy does not exist! Given strategy: " + strategyName, details);
    }
}
