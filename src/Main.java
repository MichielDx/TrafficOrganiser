import be.kdg.se3.proxy.ConveyorServiceProxy;
import main.be.kdg.bagageafhandeling.traffic.engines.RouteScheduler;
import main.be.kdg.bagageafhandeling.traffic.services.BaggageRepository;
import main.be.kdg.bagageafhandeling.traffic.services.route.RouteRepository;

import java.io.IOException;

/**
 * Created by Michiel on 5/11/2016.
 */
public class Main {
    public static void main(String[] args) {
        RouteScheduler routeScheduler = new RouteScheduler(new BaggageRepository(), new RouteRepository());
    }
}
