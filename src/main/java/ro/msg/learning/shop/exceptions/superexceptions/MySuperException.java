package ro.msg.learning.shop.exceptions.superexceptions;

import lombok.Getter;

@Getter
public class MySuperException extends RuntimeException {

    private final String details;

    public MySuperException(String message, String details) {
        super(message);
        this.details = details;
    }
}
