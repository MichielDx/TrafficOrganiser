package main.be.kdg.bagageafhandeling.traffic.services.interfaces;

import main.be.kdg.bagageafhandeling.traffic.exceptions.APIException;
import main.be.kdg.bagageafhandeling.traffic.model.flight.FlightInfo;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Michiel on 5/11/2016.
 */
public interface FlightService {
    @GET("/flightservice/{flightid}")
    public FlightInfo fetchFlightInfo(@Path("flightid") int flightID) throws APIException;
}
