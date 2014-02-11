package mobi.eyeline.ips.service;

import mobi.eyeline.ips.messages.AnswerOption;
import mobi.eyeline.ips.messages.MessageHandler;
import mobi.eyeline.ips.messages.MissingParameterException;
import mobi.eyeline.ips.messages.ParseUtils;
import mobi.eyeline.ips.messages.RegistrationOption;
import mobi.eyeline.ips.messages.UssdModel;
import mobi.eyeline.ips.messages.UssdOption;
import mobi.eyeline.ips.model.Answer;
import mobi.eyeline.ips.model.Question;
import mobi.eyeline.ips.model.QuestionOption;
import mobi.eyeline.ips.model.Respondent;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.repository.AnswerRepository;
import mobi.eyeline.ips.repository.QuestionOptionRepository;
import mobi.eyeline.ips.repository.RespondentRepository;
import mobi.eyeline.ips.repository.SurveyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static mobi.eyeline.ips.messages.ParseUtils.getInt;
import static mobi.eyeline.ips.messages.ParseUtils.getString;
import static mobi.eyeline.ips.messages.RegistrationOption.RegistrationAccepted;
import static mobi.eyeline.ips.messages.RegistrationOption.RegistrationDeclined;

/**
 * Mobilizer landing page rendering.
 */
public class UssdService implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(UssdService.class);

    private final SurveyRepository surveyRepository;
    private final RespondentRepository respondentRepository;
    private final AnswerRepository answerRepository;
    private final QuestionOptionRepository questionOptionRepository;

    /**
     * Generic non-localized messages.
     */
    private final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public UssdService(SurveyRepository surveyRepository,
                       RespondentRepository respondentRepository,
                       AnswerRepository answerRepository,
                       QuestionOptionRepository questionOptionRepository) {

        this.surveyRepository = surveyRepository;
        this.respondentRepository = respondentRepository;
        this.answerRepository = answerRepository;
        this.questionOptionRepository = questionOptionRepository;
    }

    public UssdModel handle(HttpServletRequest request)
            throws MissingParameterException {

        try {
            return handle0(request);

        } catch (RuntimeException e) {
            return new UssdModel(bundle.getString("ussd.error"));
        }
    }

    private UssdModel handle0(HttpServletRequest request)
            throws MissingParameterException {

        @SuppressWarnings("unchecked")
        final Map<String, String[]> parameters =
                (Map<String, String[]>) request.getParameterMap();

        logger.debug("Handling request: " + ParseUtils.toString(parameters));

        final String msisdn = getString(parameters, "abonent");

        final UssdOption answer = UssdOption.parse(parameters);

        if (answer == null) {
            // Respondent just loaded the start page.
            // It might be either an unregistered msisdn (new respondent),
            // survey restart or resumption.
            final int surveyId = getInt(parameters, "survey_id");
            return handleStartPage(msisdn, surveyId);

        } else {
            return answer.handle(msisdn, this);
        }
    }

    private UssdModel handleStartPage(String msisdn, int surveyId) {
        final Survey survey = surveyRepository.get(surveyId);

        // Ensure survey is valid and accessible.
        {
            if (survey == null) {
                logger.info("Survey not found for ID = [" + surveyId + "]");
                return getSurveyNotFound();

            } else if (!survey.isRunningNow()) {
                logger.info("Survey is not active now, ID = [" + surveyId + "]");
                return getSurveyNotFound();

            } else if (!survey.isActive()) {
                logger.info("Survey is disabled, ID = [" + surveyId + "]");
                return getSurveyNotFound();
            }
        }

        // Ensure we've got an entry in `respondents' for this survey.
        final Respondent respondent =
                respondentRepository.findOrCreate(msisdn, survey);

        final Answer lastAnswer =
                answerRepository.getLast(survey, respondent);
        if (lastAnswer != null) {
            // Respondent is already registered in this survey.

            final Question next = lastAnswer.getQuestion().getNext();
            if (next == null) {
                // All the questions are already answered.
                // This means survey restart, so we render a welcome message.
                return renderRestartPrompt(survey, respondent);

            } else {
                // There are unanswered questions, so render the next one.
                return renderQuestion(next);
            }

        } else {
            return renderFirstQuestion(survey, respondent);
        }
    }

    private UssdModel renderRestartPrompt(Survey survey, Respondent respondent) {
        return renderFirstQuestion(survey, respondent);
    }

    @Override
    public UssdModel handle(String msisdn, RegistrationAccepted request) {
        final Survey survey = surveyRepository.get(request.getSurveyId());
        if ((survey == null) || !survey.isRunningNow() || !survey.isActive()) {
            return getSurveyNotFound();
        }

        final Respondent respondent =
                respondentRepository.findOrCreate(msisdn, survey);

        return renderFirstQuestion(survey, respondent);
    }

    @Override
    public UssdModel handle(String msisdn, RegistrationDeclined request) {
        final Survey survey = surveyRepository.get(request.getSurveyId());
        if ((survey == null) || !survey.isRunningNow() || !survey.isActive()) {
            return getSurveyNotFound();
        }

        return new UssdModel(survey.getDetails().getEndText());
    }

    @Override
    public UssdModel handle(String msisdn, AnswerOption request) {
        final Survey survey = surveyRepository.get(request.getSurveyId());
        if ((survey == null) || !survey.isRunningNow() || !survey.isActive()) {
            return getSurveyNotFound();
        }

        final Respondent respondent =
                respondentRepository.findOrCreate(msisdn, survey);
        final QuestionOption option =
                questionOptionRepository.load(request.getAnswerId());

        answerRepository.save(respondent, option);

        final Question next = option.getQuestion().getNext();
        if (next != null) {
            // Render next question.
            return renderQuestion(next);

        } else {
            // All the questions are answered, meaning we just show end message.
            return renderEnd(option.getQuestion().getSurvey());
        }
    }

    @Override
    public UssdModel handle(String msisdn, UssdOption request) {
        throw new AssertionError("Unsupported request type: " + request.getClass());
    }

    /**
     * @return Survey welcome message.
     */
    private UssdModel renderWelcome(Survey survey) {
        // TODO: Should we make option labels here editable in the survey?
        //       This basically means adding two fixed options to the welcome message.
        return new UssdModel(
                survey.getDetails().getWelcomeText(),
                RegistrationOption.getDefaultOptions(survey, "Yes", "No"));
    }

    /**
     * @return Survey end message.
     */
    private UssdModel renderEnd(Survey survey) {
        return new UssdModel(survey.getDetails().getEndText());
    }

    /**
     * @return Form for the specified question.
     */
    private UssdModel renderQuestion(Question question) {
        final List<AnswerOption> renderedOptions = new ArrayList<>();
        {
            List<QuestionOption> questionOptions = question.getOptions();
            for (int i = 0; i < questionOptions.size(); i++) {
                renderedOptions.add(
                        new AnswerOption(i + 1, questionOptions.get(i))
                );
            }
        }

        return new UssdModel(question.getTitle(), renderedOptions);
    }

    private UssdModel renderFirstQuestion(Survey survey, Respondent respondent) {
        respondent.setAnswered(true);
        respondentRepository.update(respondent);

        answerRepository.clear(survey, respondent);

        if (survey.getQuestions().isEmpty()) {
            // This survey has no questions (if it's even allowed), so just end it.
            return renderEnd(survey);

        } else {
            // Send the first question.
            return renderQuestion(survey.getQuestions().get(0));
        }
    }

    private UssdModel getSurveyNotFound() {
        return new UssdModel(bundle.getString("ussd.survey.not.available"));
    }
}
