package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.SurveyStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyStatsRepository extends BaseRepository<SurveyStats, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(SurveyStatsRepository.class);

  public SurveyStatsRepository(DB db) {
    super(db);
  }

}
