package main.be.kdg.bagageafhandeling.traffic.services.interfaces;

import main.be.kdg.bagageafhandeling.traffic.exceptions.APIException;
import main.be.kdg.bagageafhandeling.traffic.model.route.RouteDTO;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Michiel on 5/11/2016.
 */
public interface ConveyorService {
    @GET("/conveyorservice/{conveyorIDs}")
    public RouteDTO fetchConveyor(@Path("conveyorIDs") String conveyorIDs) throws APIException;
}
