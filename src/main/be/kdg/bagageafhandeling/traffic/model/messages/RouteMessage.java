package main.be.kdg.bagageafhandeling.traffic.model.messages;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Michiel on 5/11/2016.
 */
@XmlRootElement(name="route")
public class RouteMessage {
    @XmlElement
    private int baggageID;
    @XmlElement
    private int conveyorID;

    public RouteMessage() {
    }

    public RouteMessage(int baggageID, int conveyorID) {
        this.baggageID = baggageID;
        this.conveyorID = conveyorID;
    }

    public int getBaggageID() {
        return baggageID;
    }

    public int getConveyorID() {
        return conveyorID;
    }
}
