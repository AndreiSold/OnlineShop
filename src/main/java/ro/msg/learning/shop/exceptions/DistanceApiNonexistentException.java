package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class DistanceApiNonexistentException extends MySuperException {

    public DistanceApiNonexistentException(String apiName, String details) {
        super("Given distance api is not one of our options! Given api name: " + apiName, details);
    }
}
