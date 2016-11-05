package main.be.kdg.bagageafhandeling.traffic.model.flight;

/**
 * Created by Michiel on 5/11/2016.
 */
public class FlightInfo {
    private int flightID;
    private int boardingConveyorID;

    public FlightInfo(int flightID, int boardingConveyorID) {
        this.flightID = flightID;
        this.boardingConveyorID = boardingConveyorID;
    }

    public int getFlightID() {
        return flightID;
    }

    public int getBoardingConveyorID() {
        return boardingConveyorID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public void setBoardingConveyorID(int boardingConveyorID) {
        this.boardingConveyorID = boardingConveyorID;
    }
}
