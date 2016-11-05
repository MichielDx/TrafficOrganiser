package main.be.kdg.bagageafhandeling.trafic.exceptions;

/**
 * Created by Michiel on 5/11/2016.
 */
public class MessageInputException extends Exception {
    public MessageInputException(String message, Exception e) {
        super(message, e);
    }
}
