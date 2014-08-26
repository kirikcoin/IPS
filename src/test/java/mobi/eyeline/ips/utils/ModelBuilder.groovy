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

    static Survey survey(Map _ = [:]) { new Survey(_) }

    static Survey survey(Map _, @DelegatesTo(SurveyContext) Closure closure) {
        def survey = survey(_)
        new SurveyContext(survey).with closure
        survey
    }

    static Survey survey(@DelegatesTo(SurveyContext) Closure closure) {
        new SurveyContext(null).with closure
        null
    }

    static QuestionOption option(Map _) {
        new QuestionContext(_['question'] as Question).option(_)
    }

    @InheritConstructors static class SurveyContext extends Context<Survey> {

        //
        // SurveyDetails
        //

        SurveyDetails details(Map _) {
            bind(new SurveyDetails(_)) {
                it.survey = enclosing
                enclosing.details = it
            }
        }


        //
        // SurveyStats
        //

        SurveyStats statistics(Map _) {
            bind(new SurveyStats(_)) {
                it.survey = enclosing
                enclosing.statistics = it
            }
        }

        SurveyStats statistics(Map _, @DelegatesTo(SurveyStatisticsContext) Closure closure) {
            statistics(statistics(_), closure)
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

        Question question(Map _) {
            bind(new Question(_)) {
                enclosing.questions << it
                it.survey = enclosing
            }
        }

        Question question(Map _, @DelegatesTo(QuestionContext) Closure closure) {
            question(question(_), closure)
        }

        static Question question(Question question, @DelegatesTo(QuestionContext) Closure closure) {
            new QuestionContext(question).with closure
            question
        }
    }

    @InheritConstructors static class SurveyStatisticsContext extends Context<SurveyStats> {

        AccessNumber accessNumber(Map _) {
            accessNumber(new AccessNumber(_))
        }

        AccessNumber accessNumber(AccessNumber accessNumber) {
            bind(accessNumber) {
                enclosing.accessNumber = it
            }
        }
    }

    @InheritConstructors static class QuestionContext extends Context<Question> {

        QuestionOption option(Map _) {
            bind(new QuestionOption(_)) {
                enclosing.options << it
                it.question = enclosing
            }
        }

        Question question(Map _) {
            new DeferredQuestion(_)
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
