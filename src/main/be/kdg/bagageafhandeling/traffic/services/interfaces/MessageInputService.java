package main.be.kdg.bagageafhandeling.traffic.services.interfaces;

import main.be.kdg.bagageafhandeling.traffic.exceptions.MessageInputException;

import java.util.Observer;

/**
 * A MessageInputService receives and converts a String message from a message broker
 */
public interface MessageInputService {
    /**
     * Start up by supplying a callback object
     */
    void initialize() throws MessageInputException;

    /**
     * Close all connections to this service
     */
    void shutdown() throws MessageInputException;

    void retrieve() throws MessageInputException;

    void addObserver(Observer o);
}
