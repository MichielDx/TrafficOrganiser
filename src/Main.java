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
        controller.setRouteInputMessageQueue(new RabbitMQRoute("baggageOutputQueue"));
        controller.setSensorInputMessageQueue(new RabbitMQSensor("sensorOutputQueue"));
        controller.setRouteOutputMessageQueue(new RabbitMQ("routeOutputQueue"));
        controller.setStatusOutputMessageQueue(new RabbitMQ("statusOutputQueue"));
        controller.setTimeDifference(120000);
        controller.initialize();
    }
}
