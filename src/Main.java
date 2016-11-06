import be.kdg.se3.proxy.ConveyorServiceProxy;
import main.be.kdg.bagageafhandeling.traffic.engines.RouteScheduler;
import main.be.kdg.bagageafhandeling.traffic.services.BaggageRepository;
import main.be.kdg.bagageafhandeling.traffic.services.route.RouteRepository;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;

/**
 * Created by Michiel on 5/11/2016.
 */
public class Main {
    public static void main(String[] args) {

        String path = new File("src/main/log4j.properties").getAbsolutePath();
        PropertyConfigurator.configure(path);
        RouteScheduler routeScheduler = new RouteScheduler(new BaggageRepository(), new RouteRepository());
    }
}
