package ro.msg.learning.shop.exceptions.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.msg.learning.shop.exceptions.*;

@RestControllerAdvice
public class ShopExceptionHandler {

    @ExceptionHandler(FileTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Response fileTypeMismatchException(FileTypeMismatchException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @ExceptionHandler(LocationIdDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Response locationIdDoesNotExistException(LocationIdDoesNotExistException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @ExceptionHandler(NegativeQuantityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Response negativeQuantityException(NegativeQuantityException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @ExceptionHandler(OrderTimestampInFutureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Response orderTimeStampInFutureException(OrderTimestampInFutureException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @ExceptionHandler(ShippingAdressNotInRomaniaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Response shippingAddressNotInRomaniaException(ShippingAdressNotInRomaniaException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @ExceptionHandler(StrategyInexistentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Response strategyInexistendException(StrategyInexistentException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @ExceptionHandler(SuitableLocationInexistentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Response suitableLocationInexistenException(SuitableLocationInexistentException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @ExceptionHandler(StockResultListEmptyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Response stockResultListEmptyException(StockResultListEmptyException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @Data
    @AllArgsConstructor
    class Response {
        private String message;
        private String details;
    }
}
