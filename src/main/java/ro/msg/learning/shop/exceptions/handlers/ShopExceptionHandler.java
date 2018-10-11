package ro.msg.learning.shop.exceptions.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.msg.learning.shop.exceptions.*;
import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class ShopExceptionHandler {

    @ExceptionHandler({FileTypeMismatchException.class, NegativeQuantityException.class, OrderTimestampInFutureException.class, ShippingAddressNotInRomaniaException.class, LocationPassedAsNullException.class, UsernameAlreadyExistsException.class, OrderDtoNullException.class, ProxyBadConfiguredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response badRequestExceptionsHandling(MySuperException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @ExceptionHandler({LocationNotFoundException.class, StrategyNonexistentException.class, SuitableLocationNonexistentException.class, CustomerIdNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response notFoundExceptionsHandling(MySuperException e) {
        return new Response(e.getMessage(), e.getDetails());
    }

    @ExceptionHandler(ResultedStockListEmptyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<Response> notResultedStockListEmptyException(MySuperException e) {
        return Collections.singletonList(new Response(e.getMessage(), e.getDetails()));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Response {
        private String message;
        private String details;
    }
}
