package main.be.kdg.bagageafhandeling.traffic.repository;

import main.be.kdg.bagageafhandeling.traffic.exceptions.RepositoryException;
import main.be.kdg.bagageafhandeling.traffic.model.bagage.Baggage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Michiel on 5/11/2016.
 */
public class BaggageRepository {
    private ConcurrentMap<Integer, Baggage> baggages;

    public BaggageRepository() {
        baggages = new ConcurrentHashMap<>();
    }

    public void addBagage(Baggage baggage) {
        baggages.put(baggage.getBaggageID(),baggage);
    }

    public void updateBagage(Baggage baggage) {
        baggages.replace(baggage.getBaggageID(),baggage);
    }

    public ConcurrentMap<Integer,Baggage> getBagages() {
        return baggages;
    }

    public Baggage getBaggage(int baggageId) throws RepositoryException {
        Baggage result = baggages.get(baggageId);

        if (result == null) throw new RepositoryException("Baggage with ID " + baggageId + " not found in cache.");
        return result;
    }

    public void remove(Baggage baggage){
        baggages.remove(baggage.getBaggageID());
    }
}