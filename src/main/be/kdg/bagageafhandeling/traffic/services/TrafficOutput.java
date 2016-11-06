package main.be.kdg.bagageafhandeling.traffic.services;

import main.be.kdg.bagageafhandeling.traffic.adapters.out.RabbitMQ;
import main.be.kdg.bagageafhandeling.traffic.exceptions.MessageOutputException;
import main.be.kdg.bagageafhandeling.traffic.model.messages.RouteMessage;
import main.be.kdg.bagageafhandeling.traffic.model.messages.StatusMessage;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.MessageOutputService;
import main.be.kdg.bagageafhandeling.traffic.services.route.RouteXmlService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 5/11/2016.
 */
public class TrafficOutput {
    private MessageOutputService rabbitMQRoute;
    private MessageOutputService rabbitMQStatus;
    private Logger logger = Logger.getLogger(TrafficOutput.class);
    private RouteXmlService routeXmlService;
    private StatusXmlService statusXmlService;

    public TrafficOutput(MessageOutputService route, MessageOutputService status) {
        rabbitMQRoute = route;
        rabbitMQStatus = status;
        routeXmlService = new RouteXmlService();
        statusXmlService = new StatusXmlService();
        initialize();
    }

    private void initialize() {
        try {
            rabbitMQRoute.initialize();
            rabbitMQStatus.initialize();
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }

    public void publish(RouteMessage routeMessage) {
        try {
            rabbitMQRoute.publish(routeXmlService.serialize(routeMessage));
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }

    public void publishStatus(StatusMessage statusMessage){
        try {
            rabbitMQStatus.publish(statusXmlService.serialize(statusMessage));
        } catch (MessageOutputException e) {
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
        }
    }
}
