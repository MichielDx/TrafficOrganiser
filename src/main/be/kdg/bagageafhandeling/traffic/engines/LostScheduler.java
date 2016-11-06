package main.be.kdg.bagageafhandeling.traffic.engines;
import main.be.kdg.bagageafhandeling.traffic.model.bagage.Baggage;
import main.be.kdg.bagageafhandeling.traffic.model.messages.StatusMessage;
import main.be.kdg.bagageafhandeling.traffic.services.BaggageRepository;
import main.be.kdg.bagageafhandeling.traffic.services.Publisher;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by Michiel on 6/11/2016.
 */
public class LostScheduler implements Runnable {
    private Publisher statusPublisher;
    private long frequency;
    private long timeDifference;
    private Logger logger = Logger.getLogger(LostScheduler.class);

    public LostScheduler(Publisher statusPublisher, long frequency, long timeDifference) {
        this.statusPublisher = statusPublisher;
        this.frequency = frequency;
        this.timeDifference = timeDifference;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(frequency);
                List<Baggage> baggages = BaggageRepository.getBagages();
                for (int i = 0; i < baggages.size(); i++) {
                    if ((System.currentTimeMillis() - baggages.get(i).getTimestamp().getTime()) > timeDifference) {
                        statusPublisher.publish(new StatusMessage(baggages.get(i).getBaggageID(), "lost", baggages.get(i).getConveyorID()));
                        logger.info(String.format("Removed baggage with id: %d from cache due to not receiving a sensormessage for over %d seconds", baggages.get(i).getBaggageID(), timeDifference / 1000));
                        BaggageRepository.remove(baggages.get(i));
                    }
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
