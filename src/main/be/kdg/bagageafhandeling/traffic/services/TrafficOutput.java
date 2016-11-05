package main.be.kdg.bagageafhandeling.traffic.services;

import main.be.kdg.bagageafhandeling.traffic.adapters.out.RabbitMQ;
import main.be.kdg.bagageafhandeling.traffic.exceptions.MessageOutputException;
import main.be.kdg.bagageafhandeling.traffic.model.messages.RouteMessage;
import main.be.kdg.bagageafhandeling.traffic.services.route.RouteXmlService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 5/11/2016.
 */
public class TrafficOutput {
    private RabbitMQ rabbitMQ;
    private Logger logger = Logger.getLogger(TrafficOutput.class);
    private RouteXmlService routeXmlService;

    public TrafficOutput() {
        rabbitMQ = new RabbitMQ("routeOutputQueue");
        routeXmlService = new RouteXmlService();
        initialize();
    }

    private void initialize() {
        try {
            rabbitMQ.initialize();
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }

    public void publish(RouteMessage routeMessage) {
        try {
            rabbitMQ.publish(routeXmlService.serialize(routeMessage));
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }
}
