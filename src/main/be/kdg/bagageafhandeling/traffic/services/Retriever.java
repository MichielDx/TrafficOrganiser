package main.be.kdg.bagageafhandeling.traffic.services;

import main.be.kdg.bagageafhandeling.traffic.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.MessageInputService;

import java.util.Observer;

/**
 * Created by Arthur Haelterman on 6/11/2016.
 */
public class Retriever {
    private MessageInputService rabbitMQ;
    public Retriever(MessageInputService service, Observer observer){
        rabbitMQ = service;
        rabbitMQ.addObserver(observer);
        initialize();
    }
    private void initialize(){
        try {
            rabbitMQ.initialize();
            rabbitMQ.retrieve();
        } catch (MessageInputException e) {
            e.getMessage();
        }
    }
}
