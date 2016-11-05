package main.be.kdg.bagageafhandeling.trafic.services.interfaces;

import main.be.kdg.bagageafhandeling.trafic.exceptions.MessageInputException;

import java.util.Observer;

/**
 * Created by Michiel on 5/11/2016.
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
