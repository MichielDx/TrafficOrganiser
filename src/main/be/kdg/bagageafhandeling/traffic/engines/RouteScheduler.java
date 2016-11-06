package main.be.kdg.bagageafhandeling.traffic.engines;

import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQRoute;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQSensor;
import main.be.kdg.bagageafhandeling.traffic.exceptions.APIException;
import main.be.kdg.bagageafhandeling.traffic.exceptions.RepositoryException;
import main.be.kdg.bagageafhandeling.traffic.model.bagage.Baggage;
import main.be.kdg.bagageafhandeling.traffic.model.flight.FlightInfo;
import main.be.kdg.bagageafhandeling.traffic.model.messages.RouteMessage;
import main.be.kdg.bagageafhandeling.traffic.model.messages.SensorMessage;
import main.be.kdg.bagageafhandeling.traffic.model.messages.StatusMessage;
import main.be.kdg.bagageafhandeling.traffic.model.route.Route;
import main.be.kdg.bagageafhandeling.traffic.model.route.RouteDTO;
import main.be.kdg.bagageafhandeling.traffic.services.BaggageRepository;
import main.be.kdg.bagageafhandeling.traffic.services.Publisher;
import main.be.kdg.bagageafhandeling.traffic.services.route.RouteRepository;
import main.be.kdg.bagageafhandeling.traffic.services.InputAPI;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Michiel on 5/11/2016.
 */
public class RouteScheduler implements Observer {
    private InputAPI inputAPI;
    private Publisher routePublisher;
    private Publisher statusPublisher;
    private BaggageRepository baggageRepository;
    private RouteRepository routeRepository;
    private Logger logger = Logger.getLogger(RouteScheduler.class);

    public RouteScheduler(BaggageRepository baggageRepository, RouteRepository routeRepository, InputAPI inputAPI, Publisher routePublisher, Publisher statusPublisher) {
        this.baggageRepository = baggageRepository;
        this.routeRepository = routeRepository;
        this.routePublisher = routePublisher;
        this.statusPublisher = statusPublisher;
        this.inputAPI = inputAPI;
    }

    private void doTask(Baggage baggage, int inComingConveyorID) {
        FlightInfo flightInfo = null;
        RouteDTO routeDTO = null;
        String routeIDs = "";
        List<Route> routes = null;
        Route optimalRoute = null;
        try {
            if (inComingConveyorID != baggage.getConveyorID()){
                baggage.setConveyorID(inComingConveyorID);
            }
            flightInfo = inputAPI.getFlightInfo(baggage.getFlightID());
            logger.info("Succesfully received flightInfo with ID " + flightInfo.getFlightID() + " from flightproxy");

            routeIDs += baggage.getConveyorID() + "-" + flightInfo.getBoardingConveyorID();
            logger.info("Succesfully received routeDTO for flight with " + flightInfo.getFlightID() + " from conveyorproxy");

            if (routeRepository.contains(routeIDs)) {
                routeDTO = routeRepository.getRouteDTO(routeIDs);
            } else {
                routeDTO = getRouteDTO(routeIDs);
            }
            if(routeDTO.getRoutes() == null){
                statusPublisher.publish(new StatusMessage(baggage.getBaggageID(), "undeliverable", baggage.getConveyorID()));
                baggageRepository.updateBagage(baggage);
                return;
            }
            if (baggage.getConveyorID() == flightInfo.getBoardingConveyorID()) {
                statusPublisher.publish(new StatusMessage(baggage.getBaggageID(), "arrived", baggage.getConveyorID()));
            } else {
                routeRepository.addRouteDTO(routeIDs, routeDTO);
                routes = convertRouteDTO(routeDTO);
                optimalRoute = getOptimalRoute(routes);
                routePublisher.publish(new RouteMessage(baggage.getBaggageID(), optimalRoute.getConveyorIDs().get(1)));
                baggage.setConveyorID(optimalRoute.getConveyorIDs().get(1));
            }
            baggageRepository.updateBagage(baggage);
        } catch (APIException e) {
            logger.error(e.getMessage());
        }
    }

    private RouteDTO getRouteDTO(String routeIDs) {
        RouteDTO routeDTO = null;
        try {
            routeDTO = inputAPI.getRoutes(routeIDs);
        } catch (APIException e) {
            logger.error(e.getMessage());
        }
        if (routeDTO == null) {
            try {
                Thread.sleep(5000);
                routeDTO = getRouteDTO(routeIDs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return routeDTO;
    }

    private List<Route> convertRouteDTO(RouteDTO routeDTO) {
        List<Route> routes = new ArrayList<>();
        List<Integer> conveyorIDs = new ArrayList<>();
        String[] temp;
        for (int i = 0; i < routeDTO.getRoutes().size(); i++) {
            temp = routeDTO.getRoute(i).split("-");
            for (String str : temp) {
                conveyorIDs.add(Integer.parseInt(str));
            }
            routes.add(new Route(conveyorIDs));
        }
        return routes;
    }

    private Route getOptimalRoute(List<Route> routes) {
        int min = Integer.MAX_VALUE;
        int counter;
        Route optimalRoute = null;
        for (Baggage baggage : BaggageRepository.getBagages()) {
            for (Route route : routes) {
                counter = 0;
                for (int conveyorID : route.getConveyorIDs()) {
                    if (baggage.getConveyorID() == conveyorID) {
                        counter++;
                    }
                }
                if (counter < min) {
                    min = counter;
                    optimalRoute = route;
                }
            }
        }
        return optimalRoute;
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            Method update = getClass().getMethod("update", o.getClass(), Object.class);
            update.invoke(this, o, arg);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void update(RabbitMQRoute rabbitMQRoute, Object arg) {
        Baggage baggage = (Baggage) arg;
        baggageRepository.addBagage(baggage);
        new Thread(() -> doTask(baggage,baggage.getConveyorID())).start();
    }

    public void update(RabbitMQSensor rabbitMQSensor, Object arg) {
        SensorMessage sensorMessage = (SensorMessage) arg;
        try {
            Baggage baggage = baggageRepository.getBaggage(sensorMessage.getBaggageID());
            baggage.setTimestamp(sensorMessage.getTimestamp());
            new Thread(() -> doTask(baggage,sensorMessage.getConveyorID())).start();
        } catch (RepositoryException e) {
            logger.error(e.getMessage());
        }
    }

    public void checkIfBaggageMissing(SensorMessage sensorMessage, Baggage baggage){
        if (sensorMessage.getConveyorID() != baggage.getConveyorID()){
            statusPublisher.publish(new StatusMessage(baggage.getBaggageID(), "undeliverable", baggage.getConveyorID()));
        }
    }
}
