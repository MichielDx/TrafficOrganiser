package main.be.kdg.bagageafhandeling.traffic.services;

import main.be.kdg.bagageafhandeling.traffic.model.bagage.Baggage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michiel on 5/11/2016.
 */
public class BaggageRepository {
    private static List<Baggage> baggageList;

    public BaggageRepository() {
        baggageList = new ArrayList<>();
    }

    public synchronized void addBagage(Baggage baggage) {
        baggageList.add(baggage);
    }

    public synchronized void updateBagage(Baggage baggage) {
        baggageList.set(baggage.getBaggageID(), baggage);
    }

    public static List<Baggage> getBagages() {
        return baggageList;
    }
}