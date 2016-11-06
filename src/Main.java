import main.be.kdg.bagageafhandeling.traffic.controllers.Controller;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.ConveyorServiceAPI;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.FlightServiceAPI;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQRoute;
import main.be.kdg.bagageafhandeling.traffic.adapters.in.RabbitMQSensor;

/**
 * Created by Michiel on 5/11/2016.
 */
public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.setConveyorService(new ConveyorServiceAPI());
        controller.setFlightService(new FlightServiceAPI());
        controller.setRouteMessageQueue(new RabbitMQRoute("baggageOutputQueue"));
        controller.setSensorMessageQueue(new RabbitMQSensor("sensorOutputQueue"));
        controller.initialize();
    }
}
