package main.be.kdg.bagageafhandeling.traffic.model.bagage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by Michiel on 5/11/2016.
 */
@XmlRootElement(name="bagage")
public class Baggage {
    private int bagageID;
    private int flightID;
    private int conveyorID;
    private int sensorID;
    private Date timestamp;

    public Baggage() {
    }

    @XmlElement
    public int getBaggageID() {
        return bagageID;
    }
    @XmlElement
    public int getFlightID() {
        return flightID;
    }
    @XmlElement
    public int getConveyorID() {
        return conveyorID;
    }
    @XmlElement
    public int getSensorID() {
        return sensorID;
    }
    @XmlElement
    public Date getTimestamp() {
        return timestamp;
    }

    public void setBagageID(int bagageID) {
        this.bagageID = bagageID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public void setConveyorID(int conveyorID) {
        this.conveyorID = conveyorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
