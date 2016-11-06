package main.be.kdg.bagageafhandeling.traffic.services;

import main.be.kdg.bagageafhandeling.traffic.model.messages.RouteMessage;
import main.be.kdg.bagageafhandeling.traffic.model.messages.StatusMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Created by Michiel on 6/11/2016.
 */
public class StatusXmlService {
    private JAXBContext jaxbContext;
    private Marshaller jaxbMarshaller;
    private StringWriter sw;
    private String xmlString;

    public String serialize(StatusMessage statusMessage) {
        try {
            jaxbContext = JAXBContext.newInstance(StatusMessage.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            sw = new StringWriter();
            jaxbMarshaller.marshal(statusMessage, sw);
            xmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlString;
    }
}
