package main.be.kdg.bagageafhandeling.trafic.model.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by Michiel on 5/11/2016.
 */
@XmlRootElement
public class BagageMessageDTO {
    private int bagageID;
    private int flightID;
    private int conveyorID;
    private int sensorID;
    private Date timestamp;

    public Bagage() {
    }

    @XmlElement
    public int getBagageID() {
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
}
