package main.be.kdg.bagageafhandeling.traffic.controllers;

import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQRoute;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQSensor;
import main.be.kdg.bagageafhandeling.traffic.engines.RouteScheduler;
import main.be.kdg.bagageafhandeling.traffic.services.BaggageRepository;
import main.be.kdg.bagageafhandeling.traffic.services.InputAPI;
import main.be.kdg.bagageafhandeling.traffic.services.Retriever;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.ConveyorService;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.FlightService;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.MessageInputService;
import main.be.kdg.bagageafhandeling.traffic.services.route.RouteRepository;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

/**
 * Created by Arthur Haelterman on 6/11/2016.
 */
public class Controller {
    String path = new File("src/main/log4j.properties").getAbsolutePath();
    private ConveyorService conveyorService;
    private FlightService flightService;
    private MessageInputService routeMessageQueue;
    private MessageInputService sensorMessageQueue;
    private Retriever routeMessageRetriever;
    private Retriever sensorMessageRetriever;
    private InputAPI inputAPI;
    private RouteScheduler routeScheduler;

    public Controller(){
    }

    public void initialize(){
        PropertyConfigurator.configure(path);
        inputAPI = new InputAPI(flightService,conveyorService);
        routeScheduler = new RouteScheduler(new BaggageRepository(), new RouteRepository(), inputAPI);
        routeMessageRetriever = new Retriever(routeMessageQueue,routeScheduler);
        sensorMessageRetriever = new Retriever(sensorMessageQueue,routeScheduler);
        routeMessageRetriever.initialize();
        sensorMessageRetriever.initialize();

    }

    public void setRouteMessageQueue(MessageInputService service){
        this.routeMessageQueue = service;
    }

    public void setSensorMessageQueue(MessageInputService service){
        this.sensorMessageQueue = service;
    }

    public void setConveyorService(ConveyorService conveyorService){
        this.conveyorService=conveyorService;
    }

    public void setFlightService(FlightService flightService) {
        this.flightService = flightService;
    }
}
