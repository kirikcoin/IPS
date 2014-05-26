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
import mobi.eyeline.util.jsf.components.chart.bar.Bar
import mobi.eyeline.util.jsf.components.chart.bar.BarChart
import mobi.eyeline.util.jsf.components.chart.bar.BarModel
import mobi.eyeline.util.jsf.components.chart.pie.Pie
import mobi.eyeline.util.jsf.components.chart.pie.PieChart
import mobi.eyeline.util.jsf.components.chart.pie.PieModel
import mobi.eyeline.util.jsf.components.collapsing_group.CollapsingGroup

import javax.faces.component.UIComponent
import javax.faces.component.html.HtmlOutputText
import javax.faces.component.html.HtmlPanelGrid
import javax.faces.component.html.HtmlPanelGroup

@CompileStatic
@Slf4j('logger')
class SurveyStatsController extends BaseSurveyController {

    private final AnswerRepository answerRepository = Services.instance().answerRepository
    private final RespondentRepository respondentRepository = Services.instance().respondentRepository
    private final SurveyService surveyService = Services.instance().surveyService

    HtmlPanelGroup questionGroup

    HtmlPanelGroup getQuestionGroup() { questionGroup = questionGroup?:createQuestionGroup() }

    private HtmlPanelGroup createQuestionGroup() {
        new HtmlPanelGroup().with {
            survey.activeQuestions.each { Question q -> children << createQuestionTab(q) }
            it as HtmlPanelGroup
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
                q.activeOptions.each { QuestionOption opt ->
                    children << new Pie(name: "${opt.answer}", color: colorLoop(opt))
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
                children << new HtmlOutputText(value: strings['survey.stats.answer'])
                children << new HtmlOutputText(value: strings['survey.stats.count'])

                q.activeOptions.each { QuestionOption opt ->
                    children << new HtmlPanelGroup(
                            style:      "background-color: ${colorLoop(opt)}",
                            styleClass: 'legend-color',
                            layout:     'block')
                    children << new HtmlOutputText(value: opt.answer)
                    children << new HtmlOutputText(value: answerRepository.count(opt))
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
                        name: strings['survey.stats.response.ratio.sent'] as String,
                        color: 'blue')
                children << new Bar(
                        name: strings['survey.stats.response.ratio.answered'] as String,
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
                it as UIComponent
            }
        }

        //  Group contents: grid with the charts/tables for the question.
        def createGroupContent = { Question q ->
            new HtmlPanelGrid(columns: 2, styleClass: 'question_details').with {
                children << (createOptionsRatio(q) as UIComponent)
                children << new HtmlPanelGrid().with {
                    children << (createResponseChart(q) as UIComponent)
                    children << (createResponseTable(q) as UIComponent)
                    it as UIComponent
                }
                it as UIComponent
            }
        }

        //  Shown instead of expected group contents if there are no results for this question
        //  (as empty charts seem to look ugly).
        def placeholder = {
            new HtmlOutputText(
                value: strings['survey.stats.question.no.data'],
                styleClass: 'placeholder')
        }

        return createGroup(question).with { UIComponent group ->
            group.children << ((question.sentCount == 0 ?
                    placeholder() : createGroupContent(question)) as UIComponent)
            group
        }
    }

    //
    //  Models.
    //

    BarModel getSentQuestionsRatioModel() {
        def countSentQuestions = {Survey s ->
            s.activeQuestions.collect { Question q -> q.sentCount }.sum(0) as int
        }

        def countAnswers = {Survey s ->
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

    private PieModel getOptionsRatioModel(Question question) {
        new PieModel().with {
            question.activeOptions.each { QuestionOption opt ->
                addPart("${opt.answer}", answerRepository.count(opt))
            }
            it as PieModel
        }
    }

    private BarModel getResponseRatioModel(Question question) {
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

    @SuppressWarnings("GrMethodMayBeStatic")
    private String colorLoop(QuestionOption opt) {
        def colors = ['green', '#adff2f', 'blue', 'yellow', 'black', 'magenta']
        return colors[opt.activeIndex % colors.size()]
    }
}
