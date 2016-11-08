package main.be.kdg.bagageafhandeling.traffic.services.interfaces;

import main.be.kdg.bagageafhandeling.traffic.exceptions.MessageOutputException;

/**
 * A MessageOutputService publishes a String message in a certain format to a message broker
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
