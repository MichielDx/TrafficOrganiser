package main.be.kdg.bagageafhandeling.traffic.model.route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michiel on 5/11/2016.
 */
public class Route {
    private List<Integer> conveyorIDs;

    public Route(List<Integer> conveyorIDs) {
        this.conveyorIDs = conveyorIDs;
    }

    public List<Integer> getConveyorIDs() {
        return conveyorIDs;
    }
}
