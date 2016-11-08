package main.be.kdg.bagageafhandeling.traffic.services.interfaces;

import main.be.kdg.bagageafhandeling.traffic.exceptions.APIException;
import main.be.kdg.bagageafhandeling.traffic.model.route.RouteDTO;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * A ConveyorService fetches a {@link RouteDTO} from a ConveyorServiceProxy
 */
public interface ConveyorService {
    @GET("/conveyorservice/{conveyorIDs}")
    public RouteDTO fetchConveyor(@Path("conveyorIDs") String conveyorIDs) throws APIException;
}
