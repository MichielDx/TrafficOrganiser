package main.be.kdg.bagageafhandeling.trafic.adapters.in;

/**
 * Created by Michiel on 5/11/2016.
 */

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeoutException;package main.be.kdg.bagageafhandeling.transport.adapters.in;

import com.rabbitmq.client.*;
import main.be.kdg.bagageafhandeling.trafic.exceptions.MessageInputException;
import main.be.kdg.bagageafhandeling.trafic.model.dto.BagageMessageDTO;
import main.be.kdg.bagageafhandeling.trafic.services.interfaces.MessageInputService;
import org.apache.log4j.Logger;

/**
 * Created by Michiel on 2/11/2016.
 */
public class RabbitMQ extends Observable implements MessageInputService {
    private final String queueName;
    JAXBContext jaxbContext;
    Unmarshaller jaxbUnmarshaller;
    private Connection connection;
    private Channel channel;

    private Logger logger = Logger.getLogger(RabbitMQ.class);

    public RabbitMQ(String queueName) {
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
            throw new MessageInputException("Unable to connect to RabbitMQ", e);
        }
        logger.info("Succesfully connected to RabbitMQ queue: " + queueName);
    }

    @Override
    public void shutdown() throws MessageInputException {
        try {
            channel.close();
            connection.close();
        } catch (Exception e) {
            throw new MessageInputException("Unable to close connection to RabbitMQ", e);
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
                    logger.info("Received message from RabbitMQ queue " + queueName);
                    String message = new String(body, "UTF-8");
                    logger.debug("Message content: " + message);
                    try {
                        jaxbContext = JAXBContext.newInstance(BagageMessageDTO.class);
                        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        Reader reader = new StringReader(message);
                        BagageMessageDTO messageDTO = null;
                        messageDTO = (BagageMessageDTO) jaxbUnmarshaller.unmarshal(reader);
                        setChanged();
                        notifyObservers(messageDTO);
                    } catch (Exception e) {
                        throw new IOException("Error during conversion from RabbitMQ message to BagageMessageDTO", e);
                    }
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            throw new MessageInputException("Unable to retrieve message from RabbitMQ", e);
        }

    }

}


