package main.be.kdg.bagageafhandeling.traffic.exceptions;

/**
 * Created by Michiel on 5/11/2016.
 */
public class APIException extends Exception {
    public APIException(String message, Throwable cause) {
        super(message, cause);
    }
}
