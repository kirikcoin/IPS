package mobi.eyeline.ips.service;

import mobi.eyeline.ips.messages.AnswerOption;
import mobi.eyeline.ips.messages.MessageHandler;
import mobi.eyeline.ips.messages.MissingParameterException;
import mobi.eyeline.ips.messages.UssdOption;
import mobi.eyeline.ips.messages.UssdResponseModel;
import mobi.eyeline.ips.model.Answer;
import mobi.eyeline.ips.model.Question;
import mobi.eyeline.ips.model.QuestionOption;
import mobi.eyeline.ips.model.Respondent;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.AnswerRepository;
import mobi.eyeline.ips.repository.QuestionOptionRepository;
import mobi.eyeline.ips.repository.QuestionRepository;
import mobi.eyeline.ips.repository.RespondentRepository;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.util.RequestParseUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static mobi.eyeline.ips.messages.UssdOption.PARAM_MSISDN;
import static mobi.eyeline.ips.messages.UssdOption.PARAM_SKIP_VALIDATION;
import static mobi.eyeline.ips.messages.UssdOption.PARAM_SURVEY_ID;
import static mobi.eyeline.ips.messages.UssdResponseModel.USSD_BUNDLE;
import static mobi.eyeline.ips.util.RequestParseUtils.getBoolean;
import static mobi.eyeline.ips.util.RequestParseUtils.getInt;
import static mobi.eyeline.ips.util.RequestParseUtils.getString;

/**
 * Mobilizer landing page rendering.
 */
public class UssdService implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(UssdService.class);

    private final SurveyService surveyService;
    private final SurveyRepository surveyRepository;
    private final RespondentRepository respondentRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;

    private final String baseUrl;


    public UssdService(Config config,
                       SurveyService surveyService,
                       SurveyRepository surveyRepository,
                       RespondentRepository respondentRepository,
                       AnswerRepository answerRepository,
                       QuestionRepository questionRepository,
                       QuestionOptionRepository questionOptionRepository) {

        this.surveyService = surveyService;
        this.surveyRepository = surveyRepository;
        this.respondentRepository = respondentRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.questionOptionRepository = questionOptionRepository;

        baseUrl = config.getBaseSurveyUrl();
    }

    public UssdResponseModel handle(Map<String, String[]> parameters)
            throws MissingParameterException {

        final StopWatch timer;
        if (logger.isTraceEnabled()) {
            timer = new StopWatch();
            timer.start();

        } else {
            timer = null;
        }

        UssdResponseModel response = null;
        try {
            response = handle0(parameters);

        } catch (RuntimeException e) {
            logger.error("Error processing USSD request, parameters: " +
                    RequestParseUtils.toString(parameters), e);
            response = fatalError();

        } finally {
            if (timer != null) {
                logger.trace("USSD request, millis: " + timer.getTime() +
                        ". Request: " + RequestParseUtils.toString(parameters) + "," +
                        " response: [" + response + "]");
            }
        }

        return response;
    }

    private UssdResponseModel handle0(Map<String, String[]> parameters)
            throws MissingParameterException {

        logger.debug("Handling request: " + RequestParseUtils.toString(parameters));

        final String msisdn = getString(parameters, PARAM_MSISDN);

        final UssdOption request = UssdOption.parse(parameters);

        if (request == null) {
            // Respondent just loaded the start page.
            // It might be either an unregistered msisdn (new respondent),
            // survey surveyStart or resumption.
            final int surveyId = getInt(parameters, PARAM_SURVEY_ID);
            final boolean skipValidation = getBoolean(parameters, PARAM_SKIP_VALIDATION, false);
            return handleStartPage(msisdn, surveyId, skipValidation);

        } else {
            return request.handle(msisdn, this);
        }
    }

    private UssdResponseModel handleStartPage(String msisdn,
                                              int surveyId,
                                              boolean skipValidation) {
        final Survey survey = surveyService.findSurvey(surveyId, skipValidation);
        if (survey == null) {
            return surveyNotFound();
        }

        // Ensure we've got an entry in `respondents' for this survey.
        final Respondent respondent =
                respondentRepository.findOrCreate(msisdn, survey);

        final Answer lastAnswer =
                answerRepository.getLast(survey, respondent);

        if (!respondent.isFinished() && lastAnswer != null) {
            final Question next = lastAnswer.getQuestion().getNext();
            if (next != null) {
                // There are unanswered questions, so render the next one.
                return question(next, skipValidation);
            }
        }

        return surveyStart(survey, respondent, skipValidation);
    }

    @Override
    public UssdResponseModel handle(String msisdn, AnswerOption request) {
        final Survey survey =
                surveyService.findSurvey(request.getSurveyId(), request.isSkipValidation());
        if (survey == null) {
            return surveyNotFound();
        }

        final Respondent respondent =
                respondentRepository.findOrCreate(msisdn, survey);

        respondent.setAnswersCount(respondent.getAnswersCount() + 1);
        respondentRepository.update(respondent);

        final QuestionOption option =
                questionOptionRepository.load(request.getAnswerId());
        answerRepository.save(respondent, option);

        if (option.isTerminal()) {
            return surveyFinish(respondent, survey);
        }

        final Question next = option.getQuestion().getNext();
        if (next != null) {
            return question(next, request.isSkipValidation());

        } else {
            // All the questions are answered.
            return surveyFinish(respondent, option.getQuestion().getSurvey());
        }
    }

    @Override
    public UssdResponseModel handle(String msisdn, UssdOption request) {
        throw new AssertionError("Unsupported request type: " + request.getClass());
    }

    public String getSurveyUrl(Survey survey) {
        return baseUrl + "/ussd/index.jsp?survey_id=" + survey.getId();
    }


    //
    //  Message generators.
    //

    private UssdResponseModel surveyNotFound() {
        return new UssdResponseModel.NoSurveyResponseModel();
    }

    private UssdResponseModel fatalError() {
        return new UssdResponseModel.ErrorResponseModel();
    }

    private UssdResponseModel surveyFinish(Respondent respondent, Survey survey) {
        respondent.setFinished(true);
        respondentRepository.update(respondent);

        final String endText = survey.getDetails().getEndText();
        return new UssdResponseModel.TextUssdResponseModel((endText == null) ?
                USSD_BUNDLE.getString("ussd.end.text.default") :
                endText);
    }

    private UssdResponseModel surveyStart(Survey survey,
                                          Respondent respondent,
                                          boolean skipValidation) {

        // Clear all in case this is a RE-start.
        respondent.setFinished(false);
        respondent.setAnswersCount(0);
        respondent.setStartDate(new Date());
        respondentRepository.update(respondent);

        answerRepository.clear(survey, respondent);

        // XXX: Why doesn't `refresh' work here?
        survey = surveyRepository.load(survey.getId());

        final Question first = survey.getFirstQuestion();
        if (first != null) {
            return question(first, skipValidation);
        } else {
            // This survey has no questions (if it's even allowed), so just end it.
            return surveyFinish(respondent, survey);
        }
    }

    /**
     * @param skipValidation If set, all the links will contain
     *                       {@link mobi.eyeline.ips.messages.UssdOption#PARAM_SKIP_VALIDATION a flag} skipping survey validity check.
     * @return Form for the specified question.
     */
    private UssdResponseModel question(Question question, boolean skipValidation) {
        assert question.isActive() : "Sending inactive question";

        final List<AnswerOption> renderedOptions = new ArrayList<>();
        {
            List<QuestionOption> questionOptions = question.getOptions();
            for (int i = 0; i < questionOptions.size(); i++) {
                final QuestionOption option = questionOptions.get(i);
                if (option.isActive()) {
                    renderedOptions.add(
                            new AnswerOption(i + 1, option, skipValidation)
                    );
                }
            }
        }

        question.setSentCount(question.getSentCount() + 1);
        questionRepository.update(question);

        return new UssdResponseModel(question.getTitle(), renderedOptions);
    }
}
