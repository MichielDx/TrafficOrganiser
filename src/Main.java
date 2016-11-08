import main.be.kdg.bagageafhandeling.traffic.controllers.Controller;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.ConveyorServiceAPI;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.FlightServiceAPI;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQRoute;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQSensor;
import main.be.kdg.bagageafhandeling.traffic.adapters.out.RabbitMQ;

/**
 * Created by Michiel on 5/11/2016.
 */
public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.setConveyorService(new ConveyorServiceAPI());
        controller.setFlightService(new FlightServiceAPI());
        controller.setRouteInputMessageQueue(new RabbitMQRoute("baggageQueue"));
        controller.setSensorInputMessageQueue(new RabbitMQSensor("sensorQueue"));
        controller.setRouteOutputMessageQueue(new RabbitMQ("routeQueue"));
        controller.setStatusOutputMessageQueue(new RabbitMQ("statusQueue"));
        controller.setTimeDifference(120000);
        controller.setClearCacheTime(21600000);
        controller.initialize();
    }
}
