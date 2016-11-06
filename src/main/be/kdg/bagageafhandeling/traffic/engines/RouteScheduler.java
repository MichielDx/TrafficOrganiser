package main.be.kdg.bagageafhandeling.traffic.engines;

import main.be.kdg.bagageafhandeling.traffic.adapters.in.ConveyorServiceAPI;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.FlightServiceAPI;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQRoute;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQSensor;
import main.be.kdg.bagageafhandeling.traffic.exceptions.APIException;
import main.be.kdg.bagageafhandeling.traffic.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.traffic.exceptions.RepositoryException;
import main.be.kdg.bagageafhandeling.traffic.model.bagage.Baggage;
import main.be.kdg.bagageafhandeling.traffic.model.flight.FlightInfo;
import main.be.kdg.bagageafhandeling.traffic.model.messages.RouteMessage;
import main.be.kdg.bagageafhandeling.traffic.model.messages.SensorMessage;
import main.be.kdg.bagageafhandeling.traffic.model.route.Route;
import main.be.kdg.bagageafhandeling.traffic.model.route.RouteDTO;
import main.be.kdg.bagageafhandeling.traffic.services.BaggageRepository;
import main.be.kdg.bagageafhandeling.traffic.services.TrafficOutput;
import main.be.kdg.bagageafhandeling.traffic.services.route.RouteRepository;
import main.be.kdg.bagageafhandeling.traffic.services.TrafficInput;
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
    private TrafficInput trafficInput;
    private TrafficOutput trafficOutput;
    private BaggageRepository baggageRepository;
    private RouteRepository routeRepository;
    private Logger logger = Logger.getLogger(RouteScheduler.class);

    public RouteScheduler(BaggageRepository baggageRepository, RouteRepository routeRepository) {
        this.baggageRepository = baggageRepository;
        this.routeRepository = routeRepository;
        this.trafficInput = new TrafficInput();
        this.trafficOutput = new TrafficOutput();
        initialize();
    }

    public void initialize() {
        trafficInput.initializeFlightServiceAPI(new FlightServiceAPI());
        trafficInput.initializeConveyorServiceAPI(new ConveyorServiceAPI());
    }

    private void doTask(Baggage baggage) {
        FlightInfo flightInfo = null;
        RouteDTO routeDTO = null;
        String routeIDs = "";
        List<Route> routes = null;
        Route optimalRoute = null;
        try {
            flightInfo = trafficInput.getFlightInfo(baggage.getFlightID());
            logger.info("Succesfully received flightInfo with ID " + flightInfo.getFlightID() + " from flightproxy");
            routeIDs += baggage.getConveyorID() + "-" + flightInfo.getBoardingConveyorID();
            logger.info("Succesfully received routeDTO for flight with " + flightInfo.getFlightID() + " from conveyorproxy");
            if (routeRepository.contains(routeIDs)) {
                routeDTO = routeRepository.getRouteDTO(routeIDs);
            } else {
                routeDTO = getRouteDTO(routeIDs);
            }
            routeRepository.addRouteDTO(routeIDs, routeDTO);
            routes = convertRouteDTO(routeDTO);
            optimalRoute = getOptimalRoute(routes);
            trafficOutput.publish(new RouteMessage(baggage.getBaggageID(),optimalRoute.getConveyorIDs().get(1)));
        } catch (APIException e) {
            logger.error(e.getMessage());
        }
    }

    private RouteDTO getRouteDTO(String routeIDs) {
        RouteDTO routeDTO = null;
        try {
            return trafficInput.getRoutes(routeIDs);
        } catch (APIException e) {
            logger.error(e.getMessage());
            try {
                Thread.sleep(5000);
                routeDTO = getRouteDTO(routeIDs);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
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
        try{
            Method update = getClass().getMethod("update",o.getClass(), Object.class);
            update.invoke(this, o, arg);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void update(RabbitMQRoute rabbitMQRoute, Object arg){
        Baggage baggage = (Baggage) arg;
        baggageRepository.addBagage(baggage);
        new Thread(() -> doTask(baggage)).start();
    }

    public void update(RabbitMQSensor rabbitMQSensor, Object arg){
        SensorMessage sensorMessage = (SensorMessage) arg;
        try {
            Baggage baggage = baggageRepository.getBaggage(sensorMessage.getBaggageID());
            new Thread(() -> doTask(baggage)).start();
        } catch (RepositoryException e) {
            logger.error(e.getMessage());
        }


    }
}
