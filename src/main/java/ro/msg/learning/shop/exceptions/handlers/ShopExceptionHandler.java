package ro.msg.learning.shop.exceptions.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.msg.learning.shop.exceptions.*;
import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

@RestControllerAdvice
public class ShopExceptionHandler {

    @ExceptionHandler({FileTypeMismatchException.class, NegativeQuantityException.class, OrderTimestampInFutureException.class, ShippingAddressNotInRomaniaException.class, LocationPassedAsNullException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Response badRequestExceptionsHandling(MySuperException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @ExceptionHandler({LocationNotFoundException.class, StrategyNonexistentException.class, SuitableLocationNonexistentException.class, ResultedStockListEmptyException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Response notFoundExceptionsHandling(MySuperException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @Data
    @AllArgsConstructor
    private class Response {
        private String message;
        private String details;
    }
}
