package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.SurveyService
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.util.jsf.components.chart.bar.BarModel
import mobi.eyeline.util.jsf.components.chart.pie.PieModel

import javax.faces.bean.ManagedBean

@CompileStatic
@Slf4j('logger')
@ManagedBean(name = "surveyStatsController")
class SurveyStatsController extends BaseSurveyReadOnlyController {

  private final AnswerRepository answerRepository = Services.instance().answerRepository
  private final RespondentRepository respondentRepository = Services.instance().respondentRepository
  private final SurveyService surveyService = Services.instance().surveyService

  //
  //  Models.
  //

  BarModel getSentQuestionsRatioModel() {
    def countSentQuestions = { Survey s ->
      s.activeQuestions.collect { Question q -> q.sentCount }.sum(0) as int
    }

    def countAnswers = { Survey s ->
      s.activeQuestions.collect { Question q -> answerRepository.count(q) }.sum(0) as int
    }

    def model = new BarModel()
    model.addSection(
        strings['survey.stats.overall.sent.questions.title']
    ).with {
      addValue(strings['survey.stats.overall.sent.questions.answered'] as String,
          countAnswers(survey))
      addValue(strings['survey.stats.overall.sent.questions.sent'] as String,
          countSentQuestions(survey))
    }

    return model
  }

  BarModel getInvitationsRatioModel() {
    BarModel model = new BarModel()

    model.addSection(
        strings['survey.stats.overall.respondents.title']
    ).with {
      addValue(strings['survey.stats.overall.respondents.invitations'] as String,
          surveyService.countInvitations(survey))
      addValue(strings['survey.stats.overall.respondents.respondents'] as String,
          respondentRepository.countBySurvey(survey))
      addValue(strings['survey.stats.overall.respondents.finished'] as String,
          respondentRepository.countFinishedBySurvey(survey))
    }

    return model
  }

  // Need this inner bean to manage AJAX requests. Survey ID is not passed in this case,
  // thus any survey-specific controller fails to instantiate.
  @ManagedBean(name = "surveyStatsHelpers")
  static class SurveyStatsHelpers extends BaseController {

    private final AnswerRepository answerRepository = Services.instance().answerRepository

    PieModel getOptionsRatioModel(Question question) {
      new PieModel().with {
        question.activeOptions.each { QuestionOption opt ->
          addPart("${opt.answer}", answerRepository.count(opt))
        }

        addPart(strings['survey.stats.arbitrary.answer'] as String,
            answerRepository.countTextAnswers(question) as Number)

        it as PieModel
      }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    String colorLoop(QuestionOption opt) {
      def colors = ['green', '#adff2f', 'blue', 'yellow', 'black', 'magenta']
      return colors[opt.activeIndex % colors.size()]
    }

    BarModel getResponseRatioModel(Question question) {
      new BarModel().with {
        addSection('').with {
          addValue(
              strings['survey.stats.response.ratio.answered'] as String,
              answerRepository.count(question))
          addValue(
              strings['survey.stats.response.ratio.sent'] as String,
              question.sentCount)
        }
        it as BarModel
      }
    }

    int count(QuestionOption opt) { answerRepository.count opt }

    int defaultAnswersCount(Question question) { answerRepository.countTextAnswers question }
  }
}
