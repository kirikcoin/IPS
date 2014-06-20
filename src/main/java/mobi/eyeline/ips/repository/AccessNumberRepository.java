package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.AccessNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessNumberRepository extends BaseRepository<AccessNumber, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AccessNumberRepository.class);

    public AccessNumberRepository(DB db) {
        super(db);
    }

}
