package main.be.kdg.bagageafhandeling.traffic.controllers;

import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQRoute;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQSensor;
import main.be.kdg.bagageafhandeling.traffic.adapters.out.RabbitMQ;
import main.be.kdg.bagageafhandeling.traffic.engines.LostScheduler;
import main.be.kdg.bagageafhandeling.traffic.engines.RouteScheduler;
import main.be.kdg.bagageafhandeling.traffic.services.*;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.ConveyorService;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.FlightService;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.MessageInputService;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.MessageOutputService;
import main.be.kdg.bagageafhandeling.traffic.services.route.RouteRepository;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

/**
 * Created by Arthur Haelterman on 6/11/2016.
 */
public class Controller {
    private String path = new File("src/main/log4j.properties").getAbsolutePath();
    private ConveyorService conveyorService;
    private FlightService flightService;
    private MessageInputService routeInputMessageQueue;
    private MessageInputService sensorInputMessageQueue;
    private MessageOutputService routeOutputMessageQueue;
    private MessageOutputService statusOutputMessageQueue;
    private Retriever routeMessageRetriever;
    private Retriever sensorMessageRetriever;
    private RouteScheduler routeScheduler;
    private LostScheduler lostScheduler;
    private long timeDifference;

    public Controller() {
    }

    public void initialize() {
        PropertyConfigurator.configure(path);
        InputAPI inputAPI = new InputAPI(flightService, conveyorService);
        Publisher routePublisher = new Publisher(routeOutputMessageQueue, new XmlServiceImpl());
        Publisher statusPublisher = new Publisher(statusOutputMessageQueue, new XmlServiceImpl());
        routeScheduler = new RouteScheduler(new BaggageRepository(), new RouteRepository(), inputAPI, routePublisher, statusPublisher);
        lostScheduler = new LostScheduler(statusPublisher, 300000, timeDifference);
        routeMessageRetriever = new Retriever(routeInputMessageQueue, routeScheduler);
        sensorMessageRetriever = new Retriever(sensorInputMessageQueue, routeScheduler);
<<<<<<< HEAD:src/main/be/kdg/bagageafhandeling/traffic/Controller.java
=======
        new Thread(lostScheduler).start();
>>>>>>> 4b08b224b7a37862e30485a86c529ca32a7bfdad:src/main/be/kdg/bagageafhandeling/traffic/controllers/Controller.java
    }

    public void setRouteInputMessageQueue(MessageInputService service) {
        this.routeInputMessageQueue = service;
    }

    public void setSensorInputMessageQueue(MessageInputService service) {
        this.sensorInputMessageQueue = service;
    }

    public void setConveyorService(ConveyorService conveyorService) {
        this.conveyorService = conveyorService;
    }

    public void setFlightService(FlightService flightService) {
        this.flightService = flightService;
    }

    public void setStatusOutputMessageQueue(MessageOutputService statusOutputMessageQueue) {
        this.statusOutputMessageQueue = statusOutputMessageQueue;
    }

    public void setRouteOutputMessageQueue(MessageOutputService routeOutputMessageQueue) {
        this.routeOutputMessageQueue = routeOutputMessageQueue;
    }

    public void setTimeDifference(long timeDifference) {
        this.timeDifference = timeDifference;
    }
}
