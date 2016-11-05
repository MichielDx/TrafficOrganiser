package main.be.kdg.bagageafhandeling.traffic.services.route;

import main.be.kdg.bagageafhandeling.traffic.model.messages.RouteMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.handler.MessageContext;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by Michiel on 5/11/2016.
 */
public class RouteXmlService {
    private JAXBContext jaxbContext;
    private Marshaller jaxbMarshaller;
    private Unmarshaller jaxbUnmarshaller;
    private StringReader reader;
    private StringWriter sw;
    private String xmlString;

    public String serialize(RouteMessage routeMessage) {
        try {
            jaxbContext = JAXBContext.newInstance(RouteMessage.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            sw = new StringWriter();
            jaxbMarshaller.marshal(routeMessage, sw);
            xmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlString;
    }
}
