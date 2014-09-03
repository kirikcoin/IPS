package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.Respondent
import mobi.eyeline.ips.model.Survey

import static mobi.eyeline.ips.utils.SurveyBuilder.answers
import static mobi.eyeline.ips.utils.SurveyBuilder.surveys

class SampleAnswers {

    def survey = { int id -> surveyRepository.load(id) as Survey}
    def respondent = { int id -> respondentRepository.load(id) as Respondent }

    final Date now = new Date(1401715950_000)

    void fillTestData() {

        def allSurveys = surveys(startDate: new Date(), endDate: new Date()) {
            survey(id: 1) {
                questions {
                    question(title: 'First \\one', sentCount: 5) {
                        option(answer: 'Option \n\r1')
                        option(answer: 'Option 2')
                        option(answer: 'Option 3')
                    }

                    question(title: 'Second \none', sentCount: 5) {
                        option(answer: 'Option 1')
                        option(answer: 'Option 2')
                        option(answer: 'Option 3')
                    }
                }
            }

            survey(id: 2) {
                questions {
                    question(title: 'Third one') {
                        option(answer: 'Option 1')
                        option(answer: 'Option 2')
                        option(answer: 'Option 3')
                    }
                }
            }

            survey(id: 3) {}
            survey(id: 4, active: false) {}
        }

        allSurveys.collect { surveyRepository.save it; it }


        [
                new Respondent(id: 1, startDate: now + 1, survey: allSurveys[0], finished: false),
                new Respondent(id: 2, startDate: now + 2, survey: allSurveys[0], finished: false),
                new Respondent(id: 3, startDate: now + 3, survey: allSurveys[1], finished: true),
                new Respondent(id: 4, startDate: now + 4, survey: allSurveys[1], finished: false),
        ].each {r ->
            r.msisdn = "7913000000${r.id}"
            respondentRepository.save r
        }

        [
                answers(question: survey(1).questions[0]) {
                    answer(date: now + 1, option: survey(1).questions[0].options[0], respondent: respondent(1))
                    answer(date: now + 2, option: survey(1).questions[0].options[0], respondent: respondent(2))
                    answer(date: now + 3, option: survey(1).questions[0].options[1], respondent: respondent(3))
                },

                answers(question: survey(1).questions[1]) {
                    answer(date: now + 4, option: survey(1).questions[1].options[0], respondent: respondent(1))
                    answer(date: now + 5, option: survey(1).questions[1].options[2], respondent: respondent(2))
                },

                answers(question: survey(2).questions[0]) {
                    answer(date: now + 6, option: survey(2).questions[0].options[2], respondent: respondent(1))
                    answer(date: now + 7, option: survey(2).questions[0].options[1], respondent: respondent(2))
                },
        ].flatten().each { answerRepository.save it }
    }
}
