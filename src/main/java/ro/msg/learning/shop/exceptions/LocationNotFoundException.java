package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class LocationNotFoundException extends MySuperException {

    public LocationNotFoundException(int locationId, String details) {
        super("Given location id does not exist. This is the given value: " + locationId, details);
    }
}
