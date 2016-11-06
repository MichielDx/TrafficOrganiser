import be.kdg.se3.proxy.ConveyorServiceProxy;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQRoute;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQSensor;
import main.be.kdg.bagageafhandeling.traffic.engines.RouteScheduler;
import main.be.kdg.bagageafhandeling.traffic.services.BaggageRepository;
import main.be.kdg.bagageafhandeling.traffic.services.Retriever;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.MessageInputService;
import main.be.kdg.bagageafhandeling.traffic.services.route.RouteRepository;

import java.io.IOException;

/**
 * Created by Michiel on 5/11/2016.
 */
public class Main {
    public static void main(String[] args) {
        MessageInputService rabbitMQSensor;
        RouteScheduler routeScheduler = new RouteScheduler(new BaggageRepository(), new RouteRepository());
        Retriever routeMessageRetriever = new Retriever(new RabbitMQRoute("baggageOutputQueue"),routeScheduler);
        Retriever sensorMessageRetriever = new Retriever(new RabbitMQSensor("sensorOutputQueue"),routeScheduler);

    }
}
