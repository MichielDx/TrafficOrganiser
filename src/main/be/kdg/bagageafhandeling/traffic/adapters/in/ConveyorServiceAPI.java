package main.be.kdg.bagageafhandeling.traffic.adapters.in;

import be.kdg.se3.proxy.ConveyorServiceProxy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.be.kdg.bagageafhandeling.traffic.exceptions.APIException;
import main.be.kdg.bagageafhandeling.traffic.model.route.RouteDTO;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.ConveyorService;
import retrofit2.Retrofit;
import retrofit2.http.Path;

import java.io.IOException;

/**
 * Created by Michiel on 5/11/2016.
 */
public class ConveyorServiceAPI implements ConveyorService {
    private Retrofit retrofit;
    private RouteDTO routeDTO;
    private ConveyorServiceProxy conveyorServiceProxy;
    private Gson gson;

    public ConveyorServiceAPI() {
        conveyorServiceProxy = new ConveyorServiceProxy();
        gson = new Gson();
        /* retrofit = new Retrofit.Builder().baseUrl("http://www.services4se3.com/")
                .build();
        conveyorService = retrofit.create(FlightService.class);*/
    }


    @Override
    public RouteDTO fetchConveyor(@Path("conveyorIDs") String conveyorIDs) throws APIException {
        try {
            String result = conveyorServiceProxy.get("www.services4se3.com/conveyorservice/route/" + conveyorIDs);
            routeDTO = gson.fromJson(result, RouteDTO.class);
        } catch (IOException | JsonSyntaxException e ) {
            throw new APIException("Error when trying to convert from json to conveyor", e);
        }
        return routeDTO;
    }
}
