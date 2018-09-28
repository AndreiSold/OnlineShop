package ro.msg.learning.shop.exceptions;

import ro.msg.learning.shop.exceptions.superexceptions.MySuperException;

public class FileTypeMismatchException extends MySuperException {

    public FileTypeMismatchException(String fileName, String details) {
        super("Expected: .csv file, received: " + fileName, details);
    }
}
