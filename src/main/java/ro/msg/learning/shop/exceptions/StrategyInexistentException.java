package ro.msg.learning.shop.exceptions;

public class StrategyInexistentException extends RuntimeException {

    public StrategyInexistentException(String strategyName) {
        super("Given selection strategy does not exist! Given strategy: " + strategyName);
    }
}
