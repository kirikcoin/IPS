package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.DeliveryAbonent;
import mobi.eyeline.ips.model.DeliveryAbonentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeliveryAbonentRepository extends BaseRepository<DeliveryAbonent, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryAbonentRepository.class);

    public DeliveryAbonentRepository(DB db) {
        super(db);
    }

    public void updateState(int id, DeliveryAbonentStatus state) {
        // TODO
    }
}
