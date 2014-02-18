package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.SurveyStats;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyStatsRepository extends BaseRepository<SurveyStats, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SurveyStatsRepository.class);

    public SurveyStatsRepository(DB db) {
        super(db);
    }

    public void delete(Session session, Survey survey) {
        session.createQuery("delete SurveyStats where survey = :survey")
                .setEntity("survey",survey)
                .executeUpdate();
    }

}
