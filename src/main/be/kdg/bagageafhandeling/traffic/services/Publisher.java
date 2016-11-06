package main.be.kdg.bagageafhandeling.traffic.services;

import main.be.kdg.bagageafhandeling.traffic.exceptions.MessageOutputException;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.MessageOutputService;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.XmlService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 6/11/2016.
 */
public class Publisher {
    private MessageOutputService messageOutputService;
    private XmlService xmlService;
    private Logger logger = Logger.getLogger(Publisher.class);

    public Publisher(MessageOutputService messageOutputService, XmlService xmlService) {
        this.messageOutputService = messageOutputService;
        this.xmlService = xmlService;
        initialize();
    }

    private void initialize(){
        try {
            this.messageOutputService.initialize();
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }

    public void publish(Object object) {
        try {
            messageOutputService.publish(xmlService.serialize(object));
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }
}
