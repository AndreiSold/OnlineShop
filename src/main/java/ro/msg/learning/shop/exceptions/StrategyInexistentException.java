package ro.msg.learning.shop.exceptions;

import lombok.Data;

@Data
public class StrategyInexistentException extends RuntimeException {

    private String details;

    public StrategyInexistentException(String strategyName, String details) {
        super("Given selection strategy does not exist! Given strategy: " + strategyName);
        this.details = details;
    }
}
