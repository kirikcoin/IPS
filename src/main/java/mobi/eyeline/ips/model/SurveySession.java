package mobi.eyeline.ips.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class SurveySession implements Serializable {

    private final Survey survey;
    private final Respondent respondent;
    private final List<Answer> answers;

    public SurveySession(Survey survey,
                         Respondent respondent,
                         List<Answer> answers) {
        this.respondent = respondent;
        this.survey = survey;
        this.answers = Collections.unmodifiableList(answers);
    }

    public Respondent getRespondent() {
        return respondent;
    }

    public Survey getSurvey() {
        return survey;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

}
