package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class LocationPassedAsNullException extends MySuperException {

    public LocationPassedAsNullException(String details) {
        super("Given location is null!", details);
    }
}
