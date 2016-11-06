import be.kdg.se3.proxy.ConveyorServiceProxy;
import main.be.kdg.bagageafhandeling.traffic.Controller;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.ConveyorServiceAPI;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.FlightServiceAPI;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQRoute;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQSensor;
import main.be.kdg.bagageafhandeling.traffic.engines.RouteScheduler;
import main.be.kdg.bagageafhandeling.traffic.services.BaggageRepository;
import main.be.kdg.bagageafhandeling.traffic.services.InputAPI;
import main.be.kdg.bagageafhandeling.traffic.services.Retriever;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.MessageInputService;
import main.be.kdg.bagageafhandeling.traffic.services.route.RouteRepository;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;

/**
 * Created by Michiel on 5/11/2016.
 */
public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.setConveyorService(new ConveyorServiceAPI());
        controller.setFlightService(new FlightServiceAPI());
        controller.setRouteMessageQueue(new RabbitMQRoute("routeOutputQueue"));
        controller.setSensorMessageQueue(new RabbitMQSensor("sensorOutputQueue"));
        controller.initialize();
    }
}
