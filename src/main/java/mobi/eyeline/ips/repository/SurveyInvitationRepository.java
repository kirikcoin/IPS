package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.SurveyInvitation;

public class SurveyInvitationRepository extends BaseRepository<SurveyInvitation, Integer> {

    public SurveyInvitationRepository(DB db) {
        super(db);
    }
}
