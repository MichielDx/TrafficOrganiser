package main.be.kdg.bagageafhandeling.traffic.services;

import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQRoute;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQSensor;
import main.be.kdg.bagageafhandeling.traffic.engines.RouteScheduler;
import main.be.kdg.bagageafhandeling.traffic.exceptions.APIException;
import main.be.kdg.bagageafhandeling.traffic.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.traffic.model.flight.FlightInfo;
import main.be.kdg.bagageafhandeling.traffic.model.route.RouteDTO;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.ConveyorService;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.FlightService;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.MessageInputService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 5/11/2016.
 */
public class TrafficInput {
    private Logger logger = Logger.getLogger(TrafficInput.class);
    private FlightService flightServiceAPI;
    private ConveyorService conveyorServiceAPI;

    public TrafficInput() {

    }

    public void initializeFlightServiceAPI(FlightService flightServiceAPI){
        this.flightServiceAPI = flightServiceAPI;
    }

    public void initializeConveyorServiceAPI(ConveyorService conveyorServiceAPI){
        this.conveyorServiceAPI = conveyorServiceAPI;
    }

    public FlightInfo getFlightInfo(int flightID) throws APIException {
        return flightServiceAPI.fetchFlightInfo(flightID);
    }

    public RouteDTO getRoutes(String conveyorIDs) throws APIException{
        return conveyorServiceAPI.fetchConveyor(conveyorIDs);
    }
}
