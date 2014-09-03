package mobi.eyeline.ips.utils

import groovy.transform.Immutable
import groovy.transform.InheritConstructors
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import mobi.eyeline.ips.model.AccessNumber
import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyDetails
import mobi.eyeline.ips.model.SurveyStats

@SuppressWarnings("GrMethodMayBeStatic")
class ModelBuilder {
     //TODO: for Respondent(RespondentRepositoryTest), Invitations(SurveyInvitationsRepositoryTest, InvitationDeliveryRepositoryTestBase)
    static Survey survey(Map args = [:]) { new Survey(args) }

    static Survey survey(Map args, @DelegatesTo(SurveyContext) Closure closure) {
        def survey = survey(args)
        new SurveyContext(survey).with closure
        survey
    }

    static Survey survey(@DelegatesTo(SurveyContext) Closure closure) {
        new SurveyContext(null).with closure
        null
    }

    static QuestionOption option(Map args) {
        new QuestionContext(args['question'] as Question).option(args)
    }

    @InheritConstructors static class SurveyContext extends Context<Survey> {

        //
        // SurveyDetails
        //

        SurveyDetails details(Map args) {
            bind(new SurveyDetails(args)) {
                it.survey = enclosing
                enclosing.details = it
            }
        }


        //
        // SurveyStats
        //

        SurveyStats statistics(Map args) {
            bind(new SurveyStats(args)) {
                it.survey = enclosing
                enclosing.statistics = it
            }
        }

        SurveyStats statistics(Map args, @DelegatesTo(SurveyStatisticsContext) Closure closure) {
            statistics(statistics(args), closure)
        }


        SurveyStats statistics(SurveyStats statistics, @DelegatesTo(SurveyStatisticsContext) Closure closure) {
            new SurveyStatisticsContext(statistics).with closure
            statistics
        }


        //
        // Question
        //

        void questions(@DelegatesTo(SurveyContext) Closure closure) {
            this.with closure
        }

        Question question(Map args) {
            bind(new Question(args)) {
                enclosing.questions << it
                it.survey = enclosing
            }
        }

        Question question(Map args, @DelegatesTo(QuestionContext) Closure closure) {
            question(question(args), closure)
        }

        static Question question(Question question, @DelegatesTo(QuestionContext) Closure closure) {
            new QuestionContext(question).with closure
            question
        }
    }

    @InheritConstructors static class SurveyStatisticsContext extends Context<SurveyStats> {

        AccessNumber accessNumber(Map args) {
            accessNumber(new AccessNumber(args))
        }

        AccessNumber accessNumber(AccessNumber accessNumber) {
            bind(accessNumber) {
                enclosing.accessNumber = it
            }
        }
    }

    @InheritConstructors static class QuestionContext extends Context<Question> {

        QuestionOption option(Map args) {
            bind(new QuestionOption(args)) {
                enclosing.options << it
                it.question = enclosing
            }
        }

        Question question(Map args) {
            new DeferredQuestion(args)
        }
    }


    @Immutable static class DeferredQuestion extends Question {
        Map classifier
    }

    static abstract class Context<T> {
        final T enclosing

        Context(T enclosing) { this.enclosing = enclosing }

        protected <J> J bind(J obj, @ClosureParams(FirstParam) Closure closure) {
            if (enclosing) closure.call(obj)
            obj
        }
    }
}
