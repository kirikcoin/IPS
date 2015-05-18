package mobi.eyeline.ips.service;

import mobi.eyeline.ips.model.AccessNumber;
import mobi.eyeline.ips.model.ExtLinkPage;
import mobi.eyeline.ips.model.Page;
import mobi.eyeline.ips.model.Question;
import mobi.eyeline.ips.model.QuestionOption;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.repository.AccessNumberRepository;
import mobi.eyeline.ips.repository.ExtLinkPageRepository;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import mobi.eyeline.ips.repository.QuestionOptionRepository;
import mobi.eyeline.ips.repository.QuestionRepository;
import mobi.eyeline.ips.repository.SurveyInvitationRepository;
import mobi.eyeline.ips.repository.SurveyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

public class SurveyService {

  private static final Logger logger = LoggerFactory.getLogger(SurveyService.class);

  private final SurveyRepository surveyRepository;
  private final QuestionRepository questionRepository;
  private final ExtLinkPageRepository extLinkPageRepository;
  private final QuestionOptionRepository questionOptionRepository;
  private final SurveyInvitationRepository surveyInvitationRepository;
  private final InvitationDeliveryRepository invitationDeliveryRepository;
  private final AccessNumberRepository accessNumberRepository;

  public SurveyService(SurveyRepository surveyRepository,
                       QuestionRepository questionRepository,
                       ExtLinkPageRepository extLinkPageRepository,
                       QuestionOptionRepository questionOptionRepository,
                       SurveyInvitationRepository surveyInvitationRepository,
                       InvitationDeliveryRepository invitationDeliveryRepository,
                       AccessNumberRepository accessNumberRepository) {
    this.surveyRepository = surveyRepository;
    this.questionRepository = questionRepository;
    this.extLinkPageRepository = extLinkPageRepository;
    this.questionOptionRepository = questionOptionRepository;
    this.surveyInvitationRepository = surveyInvitationRepository;
    this.invitationDeliveryRepository = invitationDeliveryRepository;
    this.accessNumberRepository = accessNumberRepository;
  }

  /**
   * Loads survey by ID.
   * <br/>
   * Returns {@code null} if at least one of these conditions is not met:
   * <ol>
   * <li>Survey with this ID exists,</li>
   * <li>This survey is running now,
   * i.e. current date is between {@link Survey#startDate} and {@link Survey#endDate}</li>
   * <li>Survey is not marked as deleted</li>
   * </ol>
   *
   * @param skipValidation if set, the second check is omitted.
   */
  public Survey findSurvey(int surveyId, boolean skipValidation) {
    final Survey survey = surveyRepository.get(surveyId);

    if (survey == null) {
      logger.info("Survey not found for ID = [" + surveyId + "]");
      return null;

    } else if (!skipValidation && !survey.isRunningNow()) {
      logger.info("Survey is not active now, ID = [" + surveyId + "]");
      return null;

    } else if (!survey.isActive()) {
      logger.info("Survey is disabled, ID = [" + surveyId + "]");
      return null;
    }

    return survey;
  }

  /**
   * @return Overall invitations count, including:
   * <ul>
   * <li>Obtained from MADV,</li>
   * <li>Actually performed deliveries,</li>
   * <li>Manually specified,</li>
   * </ul>
   */
  public int countInvitations(Survey survey) {
    return survey.getStatistics().getSentCount() +
        invitationDeliveryRepository.countSent(survey) +
        surveyInvitationRepository.count(survey);
  }

  /**
   * Marks question as inactive and performs DB update.
   * Nullifies referencing option links.
   */
  public void deleteQuestion(Question question) {
    for (Question ref : getReferencesTo(question)) {
      for (QuestionOption option : ref.getActiveOptions()) {
        if (question.equals(option.getNextPage())) {
          option.setNextPage(null);
          questionOptionRepository.update(option);
        }
      }
    }

    for (Question defRef : getDefaultReferencesTo(question)) {
      defRef.setDefaultPage(null);
      questionRepository.update(defRef);
    }

    question.setDeleted(true);
    questionRepository.update(question);
  }

  public void deleteExtLinkPage(ExtLinkPage page) {
    for (Question ref : getReferencesTo(page)) {
      for (QuestionOption option : ref.getActiveOptions()) {
        if (page.equals(option.getNextPage())) {
          option.setNextPage(null);
          questionOptionRepository.update(option);
        }
      }
    }

    for (Question defRef : getDefaultReferencesTo(page)) {
      defRef.setDefaultPage(null);
      questionRepository.update(defRef);
    }

    page.setDeleted(true);
    extLinkPageRepository.update(page);
  }

  public List<Question> getReferencesTo(Page page) {
    final List<Question> refs = new ArrayList<>();

    for (Question currentQuestion : page.getSurvey().getActiveQuestions()) {
      for (QuestionOption option : currentQuestion.getActiveOptions()) {
        if (page.equals(option.getNextPage())) {
          refs.add(currentQuestion);
          break;
        }
      }
    }

    return refs;
  }

  public List<Question> getDefaultReferencesTo(Page page) {
    final List<Question> refs = new ArrayList<>();

    for (Question currentQuestion : page.getSurvey().getActiveQuestions()) {
      if (currentQuestion.getDefaultPage() == null) continue;
      if (currentQuestion.isEnabledDefaultAnswer() && currentQuestion.getDefaultPage().equals(page)) {
        refs.add(currentQuestion);
      }
    }

    return refs;
  }

  public void delete(Survey survey) {
    survey.setActive(false);

    // Unbind access numbers so they get back to the common pool.
    final List<AccessNumber> associatedC2S = accessNumberRepository.list(survey);
    if (!isEmpty(associatedC2S)) {
      for (AccessNumber number : associatedC2S) {
        number.setSurveyStats(null);
        accessNumberRepository.update(number);
      }
    }

    surveyRepository.update(survey);
  }
}
