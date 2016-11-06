package main.be.kdg.bagageafhandeling.traffic.services;

import main.be.kdg.bagageafhandeling.traffic.exceptions.APIException;
import main.be.kdg.bagageafhandeling.traffic.model.flight.FlightInfo;
import main.be.kdg.bagageafhandeling.traffic.model.route.RouteDTO;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.ConveyorService;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.FlightService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 5/11/2016.
 */
public class InputAPI {
    private FlightService flightServiceAPI;
    private ConveyorService conveyorServiceAPI;

    public InputAPI(FlightService flightServiceAPI, ConveyorService conveyorServiceAPI) {
        this.flightServiceAPI = flightServiceAPI;
        this.conveyorServiceAPI = conveyorServiceAPI;

    }

    public FlightInfo getFlightInfo(int flightID) throws APIException {
        return flightServiceAPI.fetchFlightInfo(flightID);
    }

    public RouteDTO getRoutes(String conveyorIDs) throws APIException {
        return conveyorServiceAPI.fetchConveyor(conveyorIDs);
    }
}
