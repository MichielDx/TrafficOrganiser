package main.be.kdg.bagageafhandeling.traffic;

import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQRoute;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQSensor;
import main.be.kdg.bagageafhandeling.traffic.adapters.out.RabbitMQ;
import main.be.kdg.bagageafhandeling.traffic.engines.RouteScheduler;
import main.be.kdg.bagageafhandeling.traffic.services.BaggageRepository;
import main.be.kdg.bagageafhandeling.traffic.services.InputAPI;
import main.be.kdg.bagageafhandeling.traffic.services.Retriever;
import main.be.kdg.bagageafhandeling.traffic.services.TrafficOutput;
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

    public Controller() {
    }

    public void initialize() {
        PropertyConfigurator.configure(path);
        TrafficOutput trafficOutput = new TrafficOutput(routeOutputMessageQueue,statusOutputMessageQueue);
        InputAPI inputAPI = new InputAPI(flightService, conveyorService);
        routeScheduler = new RouteScheduler(new BaggageRepository(), new RouteRepository(), inputAPI, trafficOutput);
        routeMessageRetriever = new Retriever(routeInputMessageQueue, routeScheduler);
        sensorMessageRetriever = new Retriever(sensorInputMessageQueue, routeScheduler);
        routeMessageRetriever.initialize();
        sensorMessageRetriever.initialize();
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

    public void setStatusOutputMessageQueue(MessageOutputService statusOutputMessageQueue) {this.statusOutputMessageQueue = statusOutputMessageQueue;}

    public void setRouteOutputMessageQueue(MessageOutputService routeOutputMessageQueue) {this.routeOutputMessageQueue = routeOutputMessageQueue;}
}
