package main.be.kdg.bagageafhandeling.traffic.services;

import main.be.kdg.bagageafhandeling.traffic.model.messages.RouteMessage;
import main.be.kdg.bagageafhandeling.traffic.services.interfaces.XmlService;

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
public class XmlServiceImpl implements XmlService{
    private JAXBContext jaxbContext;
    private Marshaller jaxbMarshaller;
    private StringWriter sw;
    private String xmlString;

    public String serialize(Object object) {
        try {
            jaxbContext = JAXBContext.newInstance(object.getClass());
            jaxbMarshaller = jaxbContext.createMarshaller();
            sw = new StringWriter();
            jaxbMarshaller.marshal(object, sw);
            xmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlString;
    }
}
