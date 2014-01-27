package mobi.eyeline.ips.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SurveyAndUserKey implements Serializable {

    private Survey survey;

    private User user;

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SurveyAndUserKey() {}

    public SurveyAndUserKey(Survey survey, User user) {
        this.survey = survey;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyAndUserKey that = (SurveyAndUserKey) o;

        if (survey != null ? !survey.equals(that.survey) : that.survey != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = survey != null ? survey.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
