package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.SurveyAndUserKey;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.model.UserSurveyPermissions;

public class PermissionsRepository
        extends BaseRepository<UserSurveyPermissions, SurveyAndUserKey> {

    public PermissionsRepository(DB db) {
        super(db);
    }

    public UserSurveyPermissions get(Survey survey, User user) {
        return get(new SurveyAndUserKey(survey, user));
    }
}
