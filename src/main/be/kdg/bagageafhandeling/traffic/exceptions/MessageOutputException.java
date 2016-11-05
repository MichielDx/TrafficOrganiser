package main.be.kdg.bagageafhandeling.traffic.exceptions;

/**
 * Created by Michiel on 5/11/2016.
 */
public class MessageOutputException extends Exception {
    public MessageOutputException(String message, Exception e) {
        super(message, e);
    }
}
