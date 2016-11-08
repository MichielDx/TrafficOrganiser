package main.be.kdg.bagageafhandeling.traffic.engines;

import main.be.kdg.bagageafhandeling.traffic.model.bagage.Baggage;
import main.be.kdg.bagageafhandeling.traffic.model.messages.StatusMessage;
import main.be.kdg.bagageafhandeling.traffic.repository.BaggageRepository;
import main.be.kdg.bagageafhandeling.traffic.services.Publisher;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import main.be.kdg.bagageafhandeling.traffic.model.messages.SensorMessage;

/**
 * a LostScheduler sleeps for a set amount of time after which it checks the last time a {@link SensorMessage} has been received for a certain {@link Baggage}.
 * If this is longer than a set timedifference, the {@link Baggage} is removed from cache and a {@link StatusMessage} "lost" is sent through a {@link Publisher}.
 */
public class LostScheduler implements Runnable {
    private Publisher statusPublisher;
    private long frequency;
    private long timeDifference;
    private BaggageRepository baggageRepository;
    private Logger logger = Logger.getLogger(LostScheduler.class);

    public LostScheduler(BaggageRepository baggageRepository,Publisher statusPublisher, long frequency, long timeDifference) {
        this.baggageRepository = baggageRepository;
        this.statusPublisher = statusPublisher;
        this.frequency = frequency;
        this.timeDifference = timeDifference;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(frequency);
                List<Baggage> baggageList = new ArrayList<>();
                ConcurrentMap<Integer, Baggage> baggages = baggageRepository.getBagages();
                for(Baggage baggage : baggages.values()){
                    if ((System.currentTimeMillis() - baggage.getTimestamp().getTime()) > timeDifference) {{
                        baggageList.add(baggage);
                    }}
                }
                for(Baggage baggage : baggageList){
                    statusPublisher.publish(new StatusMessage(baggage.getBaggageID(), "lost", baggage.getConveyorID()));
                    logger.info(String.format("Removed baggage with id: %d from cache due to not receiving a sensormessage for over %d seconds", baggage.getBaggageID(), timeDifference / 1000));
                    baggageRepository.remove(baggage);
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
