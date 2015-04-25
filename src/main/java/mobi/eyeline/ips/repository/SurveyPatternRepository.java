package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.SurveyPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyPatternRepository extends BaseRepository<SurveyPattern, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(SurveyPatternRepository.class);

  public SurveyPatternRepository(DB db) {
    super(db);
  }


}
