package mobi.eyeline.ips.service;

import mobi.eyeline.ips.messages.AnswerOption;
import mobi.eyeline.ips.messages.BadCommandOption;
import mobi.eyeline.ips.messages.MessageHandler;
import mobi.eyeline.ips.messages.MissingParameterException;
import mobi.eyeline.ips.messages.OuterRequest;
import mobi.eyeline.ips.messages.UssdOption;
import mobi.eyeline.ips.messages.UssdResponseModel;
import mobi.eyeline.ips.messages.UssdResponseModel.OptionsResponseModel;
import mobi.eyeline.ips.messages.UssdResponseModel.TextUssdResponseModel;
import mobi.eyeline.ips.model.Answer;
import mobi.eyeline.ips.model.ExtLinkPage;
import mobi.eyeline.ips.model.OptionAnswer;
import mobi.eyeline.ips.model.Page;
import mobi.eyeline.ips.model.Question;
import mobi.eyeline.ips.model.QuestionOption;
import mobi.eyeline.ips.model.Respondent;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.SurveyDetails;
import mobi.eyeline.ips.model.TextAnswer;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.AnswerRepository;
import mobi.eyeline.ips.repository.ExtLinkPageRepository;
import mobi.eyeline.ips.repository.QuestionOptionRepository;
import mobi.eyeline.ips.repository.QuestionRepository;
import mobi.eyeline.ips.repository.RespondentRepository;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.util.RequestParseUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static mobi.eyeline.ips.messages.UssdOption.PARAM_MSISDN;
import static mobi.eyeline.ips.messages.UssdOption.PARAM_MSISDN_DEPRECATED;
import static mobi.eyeline.ips.messages.UssdOption.PARAM_SKIP_VALIDATION;
import static mobi.eyeline.ips.messages.UssdOption.PARAM_SURVEY_ID;
import static mobi.eyeline.ips.messages.UssdResponseModel.USSD_BUNDLE;

/**
 * Mobilizer landing page rendering.
 */
public class UssdService implements MessageHandler {

  private static final Logger logger = LoggerFactory.getLogger(UssdService.class);

  private final SurveyService surveyService;
  private final PushService pushService;
  private final CouponService couponService;

  private final SurveyRepository surveyRepository;
  private final RespondentRepository respondentRepository;
  private final AnswerRepository answerRepository;
  private final QuestionRepository questionRepository;
  private final QuestionOptionRepository questionOptionRepository;
  private final ExtLinkPageRepository extLinkPageRepository;

  private final String baseUrl;


  public UssdService(Config config,

                     SurveyService surveyService,
                     PushService pushService,
                     CouponService couponService,

                     SurveyRepository surveyRepository,
                     RespondentRepository respondentRepository,
                     AnswerRepository answerRepository,
                     QuestionRepository questionRepository,
                     QuestionOptionRepository questionOptionRepository,
                     ExtLinkPageRepository extLinkPageRepository) {

    this.surveyService = surveyService;
    this.pushService = pushService;
    this.couponService = couponService;

    this.surveyRepository = surveyRepository;
    this.respondentRepository = respondentRepository;
    this.answerRepository = answerRepository;
    this.questionRepository = questionRepository;
    this.questionOptionRepository = questionOptionRepository;
    this.extLinkPageRepository = extLinkPageRepository;

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
      response = handle0(new OuterRequest(parameters));

    } catch (RuntimeException e) {
      logger.error("Error processing USSD request, parameters: " +
          RequestParseUtils.toString(parameters), e);
      response = fatalError();

    } catch (MissingParameterException e) {
      logger.error("Bad USSD request, parameters: " +
          RequestParseUtils.toString(parameters), e);
      throw e;

    } finally {
      if (timer != null) {
        logger.trace("USSD request, millis: " + timer.getTime() +
            ". Request: " + RequestParseUtils.toString(parameters) + "," +
            " response: [" + response + "]");
      }
    }

    return response;
  }

  private UssdResponseModel handle0(OuterRequest origRequest)
      throws MissingParameterException {

    logger.debug("Handling request: " + origRequest.toString());

    final String msisdn = origRequest.getString(PARAM_MSISDN_DEPRECATED);

    final UssdOption request = UssdOption.parse(origRequest.getUrlParams());

    if (request == null) {
      // Respondent just loaded the start page.
      // It might be either an unregistered MSISDN (new respondent), survey restart or resumption.
      final int surveyId = origRequest.getInt(PARAM_SURVEY_ID);
      final boolean skipValidation = origRequest.getBoolean(PARAM_SKIP_VALIDATION, false);
      return handleStartPage(origRequest, msisdn, surveyId, skipValidation);

    } else {
      return request.handle(msisdn, this, origRequest);
    }
  }

  private UssdResponseModel handleStartPage(OuterRequest origRequest,
                                            String msisdn,
                                            int surveyId,
                                            boolean skipValidation) {
    final Survey survey = surveyService.findSurvey(surveyId, skipValidation);
    if (survey == null) {
      return surveyNotFound();
    }

    if (couponService.shouldGenerateCoupon(survey)) {
      if (couponService.getAvailable(survey) == 0) {
        return surveyNotFound();
      }
    }

    // Ensure we've got an entry in `respondents' for this survey.
    final Respondent respondent =
        respondentRepository.findOrCreate(msisdn, survey);

    return surveyStart(origRequest, survey, respondent, skipValidation);
  }

  @Override
  public UssdResponseModel handle(String msisdn, AnswerOption request, OuterRequest outerRequest) {
    final Survey survey =
        surveyService.findSurvey(request.getSurveyId(), request.isSkipValidation());
    if (survey == null) {
      return surveyNotFound();
    }

    if (couponService.shouldGenerateCoupon(survey)) {
      if (couponService.getAvailable(survey) == 0) {
        return surveyNotFound();
      }
    }

    final Respondent respondent =
        respondentRepository.findOrCreate(msisdn, survey);

    respondent.setAnswersCount(respondent.getAnswersCount() + 1);
    respondentRepository.update(respondent);

    final QuestionOption option =
        questionOptionRepository.load(request.getAnswerId());
    answerRepository.save(respondent, option);

    final Page next = option.getNextPage();
    if (next != null) {
      return page(outerRequest, next, request.isSkipValidation());

    } else {
      // All the questions are answered.
      return surveyFinish(outerRequest, respondent, option.getQuestion().getSurvey());
    }
  }

  @Override
  public UssdResponseModel handle(String msisdn, BadCommandOption request, OuterRequest outerRequest) {
    final Survey survey =
        surveyService.findSurvey(request.getSurveyId(), request.isSkipValidation());

    if (survey == null) {
      return surveyNotFound();
    }

    final Respondent respondent =
        respondentRepository.findOrCreate(msisdn, survey);

    final Answer lastAnswer = answerRepository.getLast(survey, respondent);

    final Page current = (lastAnswer == null) ?
        survey.getFirstQuestion() : getNextPage(lastAnswer);

    if ((current instanceof Question) && ((Question) current).isEnabledDefaultAnswer()) {
      return processDefaultQuestion(outerRequest, request, survey, respondent, (Question) current);

    } else {
      return page(outerRequest, current, request.isSkipValidation());
    }
  }

  private Page getNextPage(Answer lastAnswer) {
    final Question lastAnsweredQuestion = lastAnswer.getQuestion();

    if (lastAnswer instanceof TextAnswer) {
      return lastAnsweredQuestion.getDefaultPage();
    } else {
      return ((OptionAnswer) lastAnswer).getOption().getNextPage();
    }
  }

  private UssdResponseModel processDefaultQuestion(OuterRequest outerRequest,
                                                   BadCommandOption request,
                                                   Survey survey,
                                                   Respondent respondent,
                                                   Question question) {
    answerRepository.save(respondent, request.getAnswerText(), question);
    respondent.setAnswersCount(respondent.getAnswersCount() + 1);
    respondentRepository.update(respondent);

    final Page next = question.getDefaultPage();
    return (next == null) ?
        surveyFinish(outerRequest, respondent, survey) :
        page(outerRequest, next, request.isSkipValidation());
  }

  @Override
  public UssdResponseModel handle(String msisdn, UssdOption request, OuterRequest outerRequest) {
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

  private UssdResponseModel surveyFinish(OuterRequest origRequest,
                                         Respondent respondent,
                                         Survey survey) {

    respondent.setFinished(true);
    respondentRepository.update(respondent);

    processEndSms(respondent, survey);

    final String endText = survey.getDetails().getEndText();
    return new TextUssdResponseModel((endText == null) ?
        USSD_BUNDLE.getString("ussd.end.text.default") :
        endText);
  }

  private void processEndSms(Respondent respondent, Survey survey) {
    final SurveyDetails details = survey.getDetails();
    if (!details.isEndSmsEnabled()) {
      return;
    }

    String message = details.getEndSmsText();
    if (couponService.shouldGenerateCoupon(survey)) {

      final CharSequence coupon;
      if (respondent.getCoupon() != null) {
        coupon = respondent.getCoupon();

      } else {
        coupon = couponService.generate(survey);

        respondent.setCoupon(coupon.toString());
        respondentRepository.update(respondent);
      }

      message = message.replace(couponService.getCouponTag(), coupon);
    }

    pushService.scheduleSendSms(
        survey, details.getEndSmsFrom(), message, respondent.getMsisdn());
  }

  private UssdResponseModel surveyStart(OuterRequest origRequest,
                                        Survey survey,
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
      return question(origRequest, first, skipValidation);
    } else {
      // This survey has no questions (if it's even allowed), so just end it.
      return surveyFinish(origRequest, respondent, survey);
    }
  }

  private UssdResponseModel page(OuterRequest outerRequest, Page page, boolean skipValidation) {
    if      (page instanceof Question)    return question(outerRequest, (Question) page, skipValidation);
    else if (page instanceof ExtLinkPage) return extLink(outerRequest, (ExtLinkPage) page);

    throw new AssertionError("Unexpected page type: [" + page + "]");
  }

  /**
   * @param skipValidation If set, all the links will contain
   *                       {@link UssdOption#PARAM_SKIP_VALIDATION a flag} skipping survey validity check.
   * @return Form for the specified question.
   */
  private UssdResponseModel question(OuterRequest outerRequest,
                                     Question question,
                                     boolean skipValidation) {
    assert !question.isDeleted() : "Sending inactive question";

    final List<AnswerOption> renderedOptions = new ArrayList<>();
    for (QuestionOption option : question.getActiveOptions()) {
      final boolean isExitLink =
          (option.getNextPage() == null) &&
              (question.getSurvey().getDetails().getEndText() == null);

      renderedOptions.add(
          new AnswerOption(option.getActiveIndex() + 1, option, skipValidation, isExitLink)
      );
    }

    question.setSentCount(question.getSentCount() + 1);
    questionRepository.update(question);

    return new OptionsResponseModel(question.getTitle(), renderedOptions);
  }

  /**
   * @return Form for the specified question.
   */
  private UssdResponseModel extLink(final OuterRequest outerRequest,
                                    ExtLinkPage extLink) {

    assert !extLink.isDeleted() : "Sending inactive question";

    try {
      final URIBuilder uri = new URIBuilder(extLink.getServiceUrl()) {{
        final String paramMsisdn = outerRequest.getString(PARAM_MSISDN, null);
        if (paramMsisdn != null) addParameter(PARAM_MSISDN, paramMsisdn);

        final String paramMsisdnDeprecated = outerRequest.getString(PARAM_MSISDN_DEPRECATED, null);
        if (paramMsisdnDeprecated != null) addParameter(PARAM_MSISDN_DEPRECATED, paramMsisdnDeprecated);
      }};

      final UssdResponseModel.RedirectUssdResponseModel response =
          new UssdResponseModel.RedirectUssdResponseModel(uri.build().toString());

      extLink.setSentCount(extLink.getSentCount() + 1);
      extLinkPageRepository.update(extLink);

      return response;

    } catch (URISyntaxException e) {
      logger.error(
          "Malformed URL for external service link [" + extLink + "]: " + e.getMessage(), e);
      return fatalError();
    }
  }

}
