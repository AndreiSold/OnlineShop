package ro.msg.learning.shop.exceptions;

public class LocationIdDoesNotExistException extends RuntimeException {

    public LocationIdDoesNotExistException(int locationId) {
        super("Given location id does not exist. This is the given value: " + locationId);
    }
}
