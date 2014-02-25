package mobi.eyeline.ips.web.controllers.surveys

import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.SurveyService
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
    private final SurveyService surveyService = Services.instance().surveyService

    HtmlPanelGroup questionGroup

    HtmlPanelGroup getQuestionGroup() { questionGroup = questionGroup?:createQuestionGroup() }

    private HtmlPanelGroup createQuestionGroup() {
        new HtmlPanelGroup().with {
            survey.activeQuestions.each { children << createQuestionTab(it) }
            it
        }
    }

    private UIComponent createQuestionTab(Question question) {

        //  Pie chart with answered options ratio.
        def createOptionsRatio = { Question q ->
            new PieChart(
                    id: "optionsRatio_${q.id}",
                    width: 490,
                    height: 500,
                    pie: getOptionsRatioModel(q)
            ).with {
                q.activeOptions.each {
                    children << new Pie(name: "${it.answer}", color: colorLoop(it))
                }
                it
            }
        }

        //  Table : | color | option | respondents count |
        def createResponseTable = { Question q ->
            new HtmlPanelGrid(
                    columns: 3,
                    styleClass: 'resultsTable eyeline_list grid',
                    columnClasses: 'col1,col2,col3').with {
                children << new HtmlOutputText()
                children << new HtmlOutputText(value: resourceBundle.getString('survey.stats.answer'))
                children << new HtmlOutputText(value: resourceBundle.getString('survey.stats.count'))

                q.activeOptions.each {
                    children << new HtmlPanelGroup(
                            style:      "background-color: ${colorLoop(it)}",
                            styleClass: 'legend-color',
                            layout:     'block')
                    children << new HtmlOutputText(value: it.answer)
                    children << new HtmlOutputText(value: answerRepository.count(it))
                }
                it
            }
        }

        //  Horizontal bars, 2 rows: sent vs answered
        def createResponseChart = { Question q ->
            new BarChart(
                    id:         "responseRatio_${q.id}",
                    width:      490,
                    height:     200,
                    bars:       getResponseRatioModel(q),
                    intValues:  true,
                    stackMode:  false,
                    horizontal: true
            ).with {
                children << new Bar(
                        name: resourceBundle.getString('survey.stats.response.ratio.sent'),
                        color: 'blue')
                children << new Bar(
                        name: resourceBundle.getString('survey.stats.response.ratio.answered'),
                        color: 'green')
                it
            }
        }

        //  Collapsing group with no nested elements.
        def createGroup = { Question q ->
            new CollapsingGroup(id: "question_${q.id}").with {
                facets.put(
                        'header',
                        new HtmlOutputText(value: q.title))
                it
            }
        }

        //  Group contents: grid with the charts/tables for the question.
        def createGroupContent = { Question q ->
            new HtmlPanelGrid(columns: 2, styleClass: 'question_details').with {
                children << createOptionsRatio(q)
                children << new HtmlPanelGrid().with {
                    children << createResponseChart(q)
                    children << createResponseTable(q)
                    it
                }
                it
            }
        }

        //  Shown instead of expected group contents if there are no results for this question
        //  (as empty charts seem to look ugly).
        def placeholder = {
            new HtmlOutputText(
                value: resourceBundle.getString('survey.stats.question.no.data'),
                styleClass: 'placeholder')
        }

        return createGroup(question).with {
            children.add(question.sentCount == 0 ? placeholder() : createGroupContent(question))
            it
        }
    }

    //
    //  Models.
    //

    BarModel getSentQuestionsRatioModel() {
        def countSentQuestions = {Survey s ->
            s.activeQuestions.collect { it.sentCount }.sum(0) as int
        }

        def countAnswers = {Survey s ->
            s.activeQuestions.collect { answerRepository.count(it) }.sum(0) as int
        }

        def model = new BarModel()
        model.addSection(
                resourceBundle.getString('survey.stats.overall.sent.questions.title')
        ).with {
            addValue(resourceBundle.getString('survey.stats.overall.sent.questions.answered'),
                    countAnswers(survey))
            addValue(resourceBundle.getString('survey.stats.overall.sent.questions.sent'),
                    countSentQuestions(survey))
        }

        return model
    }

    BarModel getInvitationsRatioModel() {
        BarModel model = new BarModel()

        model.addSection(
                resourceBundle.getString('survey.stats.overall.respondents.title')
        ).with {
            addValue(resourceBundle.getString('survey.stats.overall.respondents.invitations'),
                    surveyService.countInvitations(survey))
            addValue(resourceBundle.getString('survey.stats.overall.respondents.respondents'),
                    respondentRepository.countBySurvey(survey))
            addValue(resourceBundle.getString('survey.stats.overall.respondents.finished'),
                    respondentRepository.countFinishedBySurvey(survey))
        }

        return model
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
        new BarModel().with {
            addSection('').with {
                addValue(
                        resourceBundle.getString('survey.stats.response.ratio.answered'),
                        answerRepository.count(question))
                addValue(
                        resourceBundle.getString('survey.stats.response.ratio.sent'),
                        question.sentCount)
            }
            it
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private String colorLoop(QuestionOption opt) {
        def colors = ['green', 'greenyellow', 'blue', 'yellow', 'black', 'magenta']
        return colors[opt.activeIndex % colors.size()]
    }
}
