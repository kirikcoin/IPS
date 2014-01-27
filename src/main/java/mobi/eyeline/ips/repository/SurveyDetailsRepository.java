package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.SurveyDetails;

public class SurveyDetailsRepository extends BaseRepository<SurveyDetails, Integer> {

    public SurveyDetailsRepository(DB db) {
        super(db);
    }
}
