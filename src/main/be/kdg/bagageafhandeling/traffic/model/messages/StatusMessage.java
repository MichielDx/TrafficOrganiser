package main.be.kdg.bagageafhandeling.traffic.model.messages;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Michiel on 6/11/2016.
 */
@XmlRootElement(name="status")
public class StatusMessage {
    @XmlElement
    private int baggageID;
    @XmlElement
    private String status;
    @XmlElement
    private int conveyorID;

    public StatusMessage() {
    }

    public StatusMessage(int baggageID, String status, int conveyorID) {
        this.baggageID = baggageID;
        this.status = status;
        this.conveyorID = conveyorID;
    }

    public void setBaggageID(int baggageID) {
        this.baggageID = baggageID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setConveyorID(int conveyorID) {
        this.conveyorID = conveyorID;
    }
}
