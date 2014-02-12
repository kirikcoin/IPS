package mobi.eyeline.ips.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SurveySession {
    private final Survey survey;
    private final Respondent respondent;
    private final Map<Question, Answer> answers = new LinkedHashMap<>();

    public SurveySession(Survey survey, Respondent respondent) {
        this.respondent = respondent;
        this.survey = survey;
    }

    public void add(Answer answer) {
        answers.put(answer.getQuestion(), answer);
    }

    public Respondent getRespondent() {
        return respondent;
    }

    public Survey getSurvey() {
        return survey;
    }

    public Map<Question, Answer> getAnswers() {
        return answers;
    }

    public List<Answer> getAnswersList() {
        return new ArrayList<>(getAnswers().values());
    }
}
