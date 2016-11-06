package main.be.kdg.bagageafhandeling.traffic.adapters.in;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.*;
import main.be.kdg.bagageafhandeling.traffic.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.traffic.model.bagage.Baggage;
import main.be.kdg.bagageafhandeling.traffic.model.messages.SensorMessage;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.MessageInputService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 2/11/2016.
 */
public class RabbitMQSensor extends Observable implements MessageInputService {
    private final String queueName;
    JAXBContext jaxbContext;
    Unmarshaller jaxbUnmarshaller;
    private Connection connection;
    private Channel channel;

    private Logger logger = Logger.getLogger(RabbitMQSensor.class);

    public RabbitMQSensor(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void initialize() throws MessageInputException {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);

        } catch (IOException | TimeoutException e) {
            throw new MessageInputException("Unable to connect to RabbitMQRoute", e);
        }
        logger.info("Succesfully connected to RabbitMQSensor queue: " + queueName);
    }

    @Override
    public void shutdown() throws MessageInputException {
        try {
            channel.close();
            connection.close();
        } catch (Exception e) {
            throw new MessageInputException("Unable to close connection to RabbitMQRoute", e);
        }
    }


    public void addObserver(Observer o){
        super.addObserver(o);
    }

    public void retrieve() throws MessageInputException {
        try {
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    logger.info("Received message from RabbitMQRoute queue " + queueName);
                    String message = new String(body, "UTF-8");
                    logger.debug("Message content: " + message);
                    try {
                        jaxbContext = JAXBContext.newInstance(SensorMessage.class);
                        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        Reader reader = new StringReader(message);
                        SensorMessage sensorMessage = null;
                        sensorMessage = (SensorMessage) jaxbUnmarshaller.unmarshal(reader);
                        setChanged();
                        notifyObservers(sensorMessage);
                    } catch (Exception e) {
                        throw new IOException("Error during conversion from RabbitMQRoute message to Baggage", e);
                    }
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            throw new MessageInputException("Unable to retrieve message from RabbitMQRoute", e);
        }
    }
}


