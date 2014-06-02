package mobi.eyeline.ips.service

import mobi.eyeline.ips.model.*
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.repository.SurveyRepository

class ResultsExportServiceTest extends DbTestCase {

    private AnswerRepository answerRepository
    private SurveyRepository surveyRepository
    private RespondentRepository respondentRepository
    private ResultsExportService exportService

    private final Date now = new Date(1401715950_000)

    def survey = { int id -> surveyRepository.load(id) as Survey}
    def respondent = { int id -> respondentRepository.load(id) as Respondent }

    void setUp() {
        super.setUp()

        answerRepository = new AnswerRepository(db)
        surveyRepository = new SurveyRepository(db)
        respondentRepository = new RespondentRepository(db)
        exportService = new ResultsExportService(answerRepository, 2)

        fillTestData()
    }

    private void checkCsv(List<String> expected,
                          List<String> header, Survey survey, Date from, Date to, String filter) {

        String actual = new ByteArrayOutputStream().with {
            exportService.writeCsv it, header, survey, from, to, filter
            toString 'UTF-8'
        }

        assertEquals fixEOLs(expected.join('\r\n') + '\r\n'), fixEOLs(actual)
    }

    void test1() {
        checkCsv([
                '"a";"b"',
                '"79130000002";"0";"First \\one";"0";"Option ',
                '',
                '1";"2014-06-04 20:32:30.0"',
                '"79130000002";"1";"Second ',
                'one";"2";"Option 3";"2014-06-07 20:32:30.0"',
                '"79130000002";"0";"Third one";"1";"Option 2";"2014-06-09 20:32:30.0"',
        ], ['a', 'b'], survey(1), now + 2, now + 4, null)
    }

    void test2() {
        exportService = new ResultsExportService(answerRepository, 10)

        checkCsv([
                '"a',
                '";" \\ b  "',
                '"79130000001";"0";"First \\one";"0";"Option ',
                '',
                '1";"2014-06-03 20:32:30.0"',
                '"79130000001";"1";"Second ',
                'one";"0";"Option 1";"2014-06-06 20:32:30.0"',
                '"79130000001";"0";"Third one";"2";"Option 3";"2014-06-08 20:32:30.0"',
                '"79130000002";"0";"First \\one";"0";"Option ',
                '',
                '1";"2014-06-04 20:32:30.0"',
                '"79130000002";"1";"Second ',
                'one";"2";"Option 3";"2014-06-07 20:32:30.0"',
                '"79130000002";"0";"Third one";"1";"Option 2";"2014-06-09 20:32:30.0"'
        ], ['a\r\n', ' \\ b  '], survey(1), now - 1, now + 10, '00')
    }

    void test3() {
        checkCsv([
                '"Кириллица";"Урр"" ""урр"""',
                '"79130000003";"0";"First \\one";"1";"Option 2";"2014-06-05 20:32:30.0"'
        ], ['Кириллица', 'Урр" "урр"'], survey(2), now - 1, now + 10, null)
    }

    void test4() {
        checkCsv([
                '"Кириллица";"Урр"" ""урр"""',
        ], ['Кириллица', 'Урр" "урр"'], survey(2), now + 10, now + 20, null)
    }

    private void fillTestData() {
        Survey survey1, survey2, survey3, survey4

        //noinspection GroovyUnusedAssignment
        [
                survey1 = new Survey(id: 1),
                survey2 = new Survey(id: 2),
                survey3 = new Survey(id: 3),
                survey4 = new Survey(id: 4, active: false)
        ].each { s ->
            s.startDate = new Date()
            s.endDate = new Date()
            surveyRepository.save s
        }

        [
                new Respondent(id: 1, startDate: now + 1, survey: survey1, finished: false),
                new Respondent(id: 2, startDate: now + 2, survey: survey1, finished: false),
                new Respondent(id: 3, startDate: now + 3, survey: survey2, finished: true),
                new Respondent(id: 4, startDate: now + 4, survey: survey2, finished: false),
        ].each {r ->
            r.msisdn = "7913000000${r.id}"
            respondentRepository.save r
        }

        survey1.questions.addAll([
                new Question(survey: survey1, title: "First \\one", sentCount: 5).with {
                    options << new QuestionOption(answer: "Option \n\r1", question: it)
                    options << new QuestionOption(answer: "Option 2", question: it)
                    options << new QuestionOption(answer: "Option 3", question: it)
                    it
                },

                new Question(survey: survey1, title: "Second \none", sentCount: 5).with {
                    options << new QuestionOption(answer: "Option 1", question: it)
                    options << new QuestionOption(answer: "Option 2", question: it)
                    options << new QuestionOption(answer: "Option 3", question: it)
                    it
                }])
        surveyRepository.update(survey1)

        survey2.questions << new Question(survey: survey2, title: "Third one").with {
            options << new QuestionOption(answer: "Option 1", question: it)
            options << new QuestionOption(answer: "Option 2", question: it)
            options << new QuestionOption(answer: "Option 3", question: it)
            it
        }
        surveyRepository.update(survey2)

        [
                new Answer(
                        date: now + 1,
                        option: survey(1).questions[0].options[0],
                        question: survey(1).questions[0],
                        respondent: respondent(1)),

                new Answer(
                        date: now + 2,
                        option: survey(1).questions[0].options[0],
                        question: survey(1).questions[0],
                        respondent: respondent(2)),

                new Answer(
                        date: now + 3,
                        option: survey(1).questions[0].options[1],
                        question: survey(1).questions[0],
                        respondent: respondent(3)),

                new Answer(
                        date: now + 4,
                        option: survey(1).questions[1].options[0],
                        question: survey(1).questions[1],
                        respondent: respondent(1)),

                new Answer(
                        date: now + 5,
                        option: survey(1).questions[1].options[2],
                        question: survey(1).questions[1],
                        respondent: respondent(2)),

                new Answer(
                        date: now + 6,
                        option: survey(2).questions[0].options[2],
                        question: survey(2).questions[0],
                        respondent: respondent(1)),

                new Answer(
                        date: now + 7,
                        option: survey(2).questions[0].options[1],
                        question: survey(2).questions[0],
                        respondent: respondent(2))
        ].each {a -> answerRepository.save a}
    }

}
