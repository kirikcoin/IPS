package mobi.eyeline.ips.web.controllers.surveys

import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.util.jsf.components.chart.bar.Bar
import mobi.eyeline.util.jsf.components.chart.bar.BarChart
import mobi.eyeline.util.jsf.components.chart.bar.BarModel
import mobi.eyeline.util.jsf.components.chart.pie.Pie
import mobi.eyeline.util.jsf.components.chart.pie.PieChart
import mobi.eyeline.util.jsf.components.chart.pie.PieModel
import mobi.eyeline.util.jsf.components.collapsing_group.CollapsingGroup
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.faces.component.UIComponent
import javax.faces.component.html.HtmlOutputText
import javax.faces.component.html.HtmlPanelGrid
import javax.faces.component.html.HtmlPanelGroup

class SurveyStatsController extends BaseSurveyController {
    private static final Logger logger = LoggerFactory.getLogger(SurveyStatsController)

    private final AnswerRepository answerRepository = Services.instance().answerRepository
    private final RespondentRepository respondentRepository = Services.instance().respondentRepository

    HtmlPanelGroup questionGroup

    BarModel getSentQuestionsRatioModel() {

        def countSentQuestions = {Survey s ->
            return s.activeQuestions.collect { it.sentCount }.sum()
        }

        def countAnswers = {Survey s ->
            return s.activeQuestions.collect { answerRepository.count(it) }.sum()
        }

        def model = new BarModel()
        model.addSection(
                resourceBundle.getString('survey.stats.overall.sent.questions.title')
        ).with {
            addValue("sent", countSentQuestions(survey))
            addValue("answered", countAnswers(survey))
        }

        return model
    }

    BarModel getInvitationsRatioModel() {
        BarModel model = new BarModel()

        def section = model.addSection(
                resourceBundle.getString('survey.stats.overall.respondents.title')
        )
        section.addValue("invitations", 100)    // TODO: count invitations.
        section.addValue("respondents", respondentRepository.countBySurvey(survey))
        section.addValue("finished", respondentRepository.countFinishedBySurvey(survey))

        return model
    }

    HtmlPanelGroup getQuestionGroup() {
        questionGroup = questionGroup?:createQuestionGroup()
    }

    private HtmlPanelGroup createQuestionGroup() {
        def group = new HtmlPanelGroup()
        survey.activeQuestions.each { group.children.add(createQuestionTab(it)) }
        return group
    }

    private UIComponent createQuestionTab(Question question) {
        def createOptionsRatio = { Question q ->
            new PieChart(
                    id: "optionsRatio_${q.id}",
                    width: 500,
                    height: 500,
                    pie: getOptionsRatioModel(q)
            ).with {
                q.activeOptions.each {
                    children.add new Pie(name: "${it.answer}", color: colorLoop(it.activeIndex))
                }
                it
            }
        }

        def createResponseRatio = { Question q ->
            new BarChart(
                    id: "responseRatio_${q.id}",
                    width: 500,
                    height: 500,
                    bars: getResponseRatioModel(q),
                    intValues: true,
                    stackMode: false,
                    horizontal: false
            ).with {
                children.add new Bar(name: 'sent',      color: 'blue')
                children.add new Bar(name: 'answered',  color: 'green')
                it
            }
        }

        def createGroup = { Question q ->
            new CollapsingGroup(id: "question_${q.id}").with {
                facets.put(
                        'header',
                        new HtmlOutputText(value: q.title))
                it
            }
        }

        def createGrid = { Question q ->
            new HtmlPanelGrid(columns: 2).with {
                children.add(createOptionsRatio(q))
                children.add(createResponseRatio(q))
                it
            }
        }

        def placeholder = {
            new HtmlOutputText(
                value: resourceBundle.getString('survey.stats.question.no.data'),
                styleClass: 'placeholder')
        }

        return createGroup(question).with {
            children.add(question.sentCount == 0 ? placeholder() : createGrid(question))
            it
        }
    }

    private PieModel getOptionsRatioModel(Question question) {
        new PieModel().with {
            question.activeOptions.each { opt ->
                addPart("${opt.answer}", answerRepository.count(opt))
            }
            it
        }
    }

    private BarModel getResponseRatioModel(Question question) {
        def title = resourceBundle.getString('survey.stats.question.response.ratio.title')
        new BarModel().with {
            addSection(title).with {
                addValue('sent', question.sentCount)
                addValue('answered', answerRepository.count(question))
            }
            it
        }
    }

    private String colorLoop(int idx) {
        def colors = ['green', 'blue', 'yellow', 'black', 'magenta']
        return colors[idx % colors.size()]
    }
}
