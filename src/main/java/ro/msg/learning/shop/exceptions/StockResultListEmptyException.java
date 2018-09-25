package ro.msg.learning.shop.exceptions;

import lombok.Data;

@Data
public class StockResultListEmptyException extends RuntimeException {

    private String details;

    public StockResultListEmptyException(String message, String details) {
        super(message);
        this.details = details;
    }
}
