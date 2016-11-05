package main.be.kdg.bagageafhandeling.traffic.adapters.in;

import be.kdg.se3.proxy.FlightServiceProxy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.be.kdg.bagageafhandeling.traffic.exceptions.APIException;
import main.be.kdg.bagageafhandeling.traffic.model.flight.FlightInfo;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.FlightService;
import retrofit2.Retrofit;
import retrofit2.http.Path;

import java.io.IOException;

/**
 * Created by Michiel on 5/11/2016.
 */
public class FlightServiceAPI implements FlightService{
    private FlightService flightService;
    private Retrofit retrofit;
    private FlightInfo flightInfo;
    private FlightServiceProxy flightServiceProxy;
    private Gson gson;

    public FlightServiceAPI() {
        flightServiceProxy = new FlightServiceProxy();
        gson = new Gson();
        /* retrofit = new Retrofit.Builder().baseUrl("http://www.services4se3.com/")
                .build();
        flightService = retrofit.create(FlightService.class);*/
    }


    @Override
    public FlightInfo fetchFlightInfo(@Path("flightid") int flightID) throws APIException {
        try {
            String result = flightServiceProxy.get("www.services4se3.com/flightservice/" + flightID);
            flightInfo = gson.fromJson(result, FlightInfo.class);
        } catch (IOException | JsonSyntaxException e ) {
            throw new APIException("Error when trying to convert from json to conveyor", e);
        }
        return flightInfo;
    }
}
