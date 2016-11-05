package main.be.kdg.bagageafhandeling.traffic.model.messages;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by Michiel on 5/11/2016.
 */
@XmlRootElement(name = "sensor")
public class SensorMessage {
    @XmlElement
    private int baggageID;
    @XmlElement
    private int conveyorID;
    @XmlElement
    private Date timestamp;

    public SensorMessage(int baggageID, int conveyorID, Date timestamp) {
        this.baggageID = baggageID;
        this.conveyorID = conveyorID;
        this.timestamp = timestamp;
    }

    public int getBaggageID() {
        return baggageID;
    }

    public int getConveyorID() {
        return conveyorID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setBaggageID(int baggageID) {
        this.baggageID = baggageID;
    }

    public void setConveyorID(int conveyorID) {
        this.conveyorID = conveyorID;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
