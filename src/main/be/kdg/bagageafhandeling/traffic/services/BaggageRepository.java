package main.be.kdg.bagageafhandeling.traffic.services;

import main.be.kdg.bagageafhandeling.traffic.exceptions.RepositoryException;
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
        baggageList.set(baggage.getBaggageID()-1, baggage);

    }

    public static List<Baggage> getBagages() {
        return baggageList;
    }

    public Baggage getBaggage(int baggageId) throws RepositoryException {
        Baggage result = null;
        for (Baggage baggage : baggageList) {
            if (baggage.getBaggageID() == baggageId) result = baggage;
        }
        if (result == null) throw new RepositoryException("Baggage with ID " + baggageId + " not found in cache.");
        return result;
    }

    public static synchronized void remove(Baggage baggage){
        baggageList.remove(baggage);
    }
}