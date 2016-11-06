package main.be.kdg.bagageafhandeling.traffic.services.interfaces;

import main.be.kdg.bagageafhandeling.traffic.exceptions.MessageOutputException;

/**
 * Created by Michiel on 5/11/2016.
 */
public interface MessageOutputService {
    /**
     * Start up by supplying a callback object
     */
    void initialize() throws MessageOutputException;

    void publish(String message) throws MessageOutputException;


    /**
     * Close all connections to this service
     */
    void shutdown() throws MessageOutputException;
}
