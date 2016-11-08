package main.be.kdg.bagageafhandeling.traffic.services.interfaces;

import main.be.kdg.bagageafhandeling.traffic.exceptions.APIException;
import main.be.kdg.bagageafhandeling.traffic.model.flight.FlightInfo;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * A FlightService fetches {@link FlightInfo} from a remote host
 */
public interface FlightService {
    @GET("/flightservice/{flightid}")
    public FlightInfo fetchFlightInfo(@Path("flightid") int flightID) throws APIException;
}
