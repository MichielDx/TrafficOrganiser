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
import main.be.kdg.bagageafhandeling.traffic.repository.BaggageRepository;
import main.be.kdg.bagageafhandeling.traffic.services.Publisher;
import main.be.kdg.bagageafhandeling.traffic.repository.RouteRepository;
import main.be.kdg.bagageafhandeling.traffic.services.InputAPI;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * a RouteScheduler waits until it receives a {@link Baggage} or a {@link SensorMessage} from a message broker after
 * which it starts a seperate thread to calculate what the optimal route for this baggage is {@link Route}. *
 * If the baggage has arrived at it's boarding conveyor, there will be a {@link StatusMessage} "arrived" published on a message broker.
 * In case no route can be calculated, it publishes a {@link StatusMessage} "undeliverable" to a message broker.
 * If this calculation succeeds it publishes a {@link RouteMessage} to a message broker.
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

    private void doTask(Baggage baggage) {
        FlightInfo flightInfo = null;
        RouteDTO routeDTO = null;
        List<Route> routes = null;
        Route optimalRoute = null;
        String routeIDs = "";
        logger.info("Calculating route for baggage ID=" + baggage.getBaggageID());
        try {
            flightInfo = inputAPI.getFlightInfo(baggage.getFlightID());
            routeIDs += baggage.getConveyorID() + "-" + flightInfo.getBoardingConveyorID();
            if (routeRepository.contains(routeIDs)) {
                routeDTO = routeRepository.getRouteDTO(routeIDs);
            } else {
                routeDTO = getRouteDTO(routeIDs);
            }
            if (baggage.getConveyorID() == flightInfo.getBoardingConveyorID()) {
                statusPublisher.publish(new StatusMessage(baggage.getBaggageID(), "arrived", baggage.getConveyorID()));
                baggageRepository.remove(baggage);
                logger.info("Published StatusMessage arrived for baggage ID=" + baggage.getBaggageID());
            } else if (routeDTO.getRoutes() == null) {
                statusPublisher.publish(new StatusMessage(baggage.getBaggageID(), "undeliverable", baggage.getConveyorID()));
                logger.info("Published StatusMessage undeliverable for baggage ID=" + baggage.getBaggageID());
                baggageRepository.remove(baggage);
            } else {
                routeRepository.addRouteDTO(routeIDs, routeDTO);
                routes = convertRouteDTO(routeDTO);
                optimalRoute = getOptimalRoute(routes);
                routePublisher.publish(new RouteMessage(baggage.getBaggageID(), optimalRoute.getConveyorIDs().get(1)));
                baggage.setConveyorID(optimalRoute.getConveyorIDs().get(1));
                logger.info("Published RouteMessage containing next conveyor for baggage ID=" + baggage.getBaggageID());
                baggageRepository.updateBagage(baggage);
            }
        } catch (APIException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * @param routeIDs is the range between the current conveyor and the destination conveyor
     * @return always returns a not null {@link RouteDTO} object although the routes can be null
     */
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

    /**
     * @param routeDTO is the object returned by the getRouteDTO method containing all routes from the current conveyor to the destination conveyor
     * @return a list of {@link Route} containing all conveyors from the current conveyor to the destination conveyor
     */
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

    /**
     * At first all baggages are retrieved from cache.
     * Then for each {@link Route} in routes we calculate the current number of baggages on each conveyor in the route.
     * The route with the least amount of baggages on it is chosen to be the optimal route.
     * @param routes is a list of {@link Route} containing all conveyors from the current conveyor to the destination conveyor
     * @return the optimal {@link Route} from the current conveyor to the destination conveyor
     */
    private Route getOptimalRoute(List<Route> routes) {
        int min = Integer.MAX_VALUE;
        int counter;
        Route optimalRoute = null;
        for (Baggage baggage : baggageRepository.getBagages().values()) {
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
        new Thread(() -> doTask(baggage)).start();
    }

    public void update(RabbitMQSensor rabbitMQSensor, Object arg) {
        SensorMessage sensorMessage = (SensorMessage) arg;
        try {
            Baggage baggage = baggageRepository.getBaggage(sensorMessage.getBaggageID());
            baggage.setConveyorID(sensorMessage.getConveyorID());
            baggage.setTimestamp(sensorMessage.getTimestamp());
            new Thread(() -> doTask(baggage)).start();
        } catch (RepositoryException e) {
            logger.error(e.getMessage());
        }
    }

}
