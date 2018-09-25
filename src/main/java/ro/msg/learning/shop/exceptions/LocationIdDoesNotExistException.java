package ro.msg.learning.shop.exceptions;

import lombok.Data;

@Data
public class LocationIdDoesNotExistException extends RuntimeException {

    private String details;

    public LocationIdDoesNotExistException(int locationId, String details) {
        super("Given location id does not exist. This is the given value: " + locationId);
        this.details = details;
    }
}
