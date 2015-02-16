package mobi.eyeline.ips.utils

import groovy.transform.InheritConstructors
import mobi.eyeline.ips.model.AccessNumber
import mobi.eyeline.ips.model.Answer
import mobi.eyeline.ips.model.OptionAnswer
import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyDetails
import mobi.eyeline.ips.model.SurveyInvitation
import mobi.eyeline.ips.model.SurveyStats
import mobi.eyeline.ips.model.TextAnswer
import mobi.eyeline.ips.model.User

import static mobi.eyeline.ips.utils.ModelBuilderUtils.Context
import static mobi.eyeline.ips.utils.ModelBuilderUtils.Deferred.DeferredReference
import static mobi.eyeline.ips.utils.ModelBuilderUtils.Deferred.resolve
import static mobi.eyeline.ips.utils.ModelBuilderUtils.ListContext

@SuppressWarnings("GrMethodMayBeStatic")
class SurveyBuilder {

    static Survey survey(Map _ = [:]) { new Survey(_) }

    static Survey survey(Map _, @DelegatesTo(SurveyContext) Closure closure) {
        new SurveyContext(survey(_)).invoke closure
    }

    static Survey survey(@DelegatesTo(SurveyContext) Closure closure) {
        new SurveyContext(null).invoke closure
    }

    static List<Survey> surveys(Map _, @DelegatesTo(SurveysContext) Closure closure) {
        new SurveysContext(_).invoke closure
    }

    static List<Answer> answers(Map _, @DelegatesTo(AnswersContext) Closure closure) {
        new AnswersContext(_).invoke closure
    }

    static Question question(Map _, @DelegatesTo(QuestionContext) Closure closure) {
        new QuestionsContext(new SurveyContext(null), [:]).question(_, closure)
    }

    @InheritConstructors static class SurveysContext extends ListContext<Survey> {

        Survey survey(Map _, @DelegatesTo(SurveyContext) Closure closure) {
            create(new Survey(common + _)) {
                new SurveyContext(it).with closure
            }
        }

        List<Survey> invoke(@DelegatesTo(SurveysContext) Closure closure) { super.invoke closure }
    }

    @InheritConstructors static class AnswersContext extends ListContext<Answer> {

        OptionAnswer optionAnswer(Map _) {
            add(new OptionAnswer(common + _))
        }

        TextAnswer textAnswer(Map _) {
            add(new TextAnswer(common + _))
        }

        List<Answer> invoke(@DelegatesTo(AnswersContext) Closure closure) { super.invoke closure }
    }

    @InheritConstructors static class QuestionsContext extends ListContext<Question> {

        private SurveyContext surveyContext = new SurveyContext(null)

        QuestionsContext(SurveyContext surveyContext, Map commonArgs) {
            this(commonArgs)
            this.surveyContext = surveyContext
        }

        Question question(Map _) {
            surveyContext.bind(add(new Question(_))) {
                surveyContext.enclosing.questions << it
                it.survey = surveyContext.enclosing
            }
        }

        Question question(Map args, @DelegatesTo(QuestionContext) Closure closure) {
            question(question(args), closure)
        }

        static Question question(Question question, @DelegatesTo(QuestionContext) Closure closure) {
            new QuestionContext(question).with closure
            question
        }

        List<Question> invoke(@DelegatesTo(QuestionsContext) Closure closure) {
            super.invoke closure

            // Resolve deferred references.
            list.collectMany { it.options }
                    .findAll { QuestionOption opt -> opt.nextQuestion instanceof DeferredReference }
                    .each { QuestionOption opt ->
                opt.nextQuestion = (opt.nextQuestion as DeferredReference<Question>).resolve(list)
            }

            list
        }

        /**
         * @return Question reference.
         */
        Question ref(Map classifier) { new DeferredQuestion(classifier) }
    }


    /**
     * Utilities for creation of {@link Survey} properties.
     */
    @InheritConstructors static class SurveyContext extends Context<Survey> {

        /**
         * Creates {@link SurveyDetails} and binds to the enclosing survey.
         */
        SurveyDetails details(Map args) {
            bind(new SurveyDetails(args)) {
                it.survey = enclosing
                enclosing.details = it
            }
        }

        /**
         * Creates {@link SurveyInvitation} and binds to the enclosing survey.
         */
        SurveyInvitation invitation(Map args) {
            bind(new SurveyInvitation(args)) {
                it.survey = enclosing
            }
        }

        /**
         * Creates a {@link User} and binds to the enclosing survey as an owner.
         */
        User owner(Map args) {
            bind(new User(args)) {
                it.createdSurveys = it.createdSurveys?:[]
                it.createdSurveys << enclosing
                enclosing.owner = it
            }
        }

        /**
         * Creates {@link SurveyStats} and binds to the enclosing survey.
         */
        SurveyStats statistics(Map args) {
            bind(new SurveyStats(args)) {
                it.survey = enclosing
                enclosing.statistics = it
            }
        }

        /**
         * Creates {@link SurveyStats} and binds to the enclosing survey.
         * @param closure Invoked in the corresponding {@link SurveyStatisticsContext}
         */
        SurveyStats statistics(Map args, @DelegatesTo(SurveyStatisticsContext) Closure closure) {
            SurveyStats statistics = statistics(args)
            new SurveyStatisticsContext(statistics).with closure
            statistics
        }

        /**
         * Allows to create a set of {@link Question questions} and binds them to the enclosing survey.
         */
        List<Question> questions(@DelegatesTo(QuestionsContext) Closure closure) {
            new QuestionsContext(this, [:]).invoke closure
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
    }

    static class DeferredQuestion extends Question implements DeferredReference<Question> {
        final Map classifier
        DeferredQuestion(Map classifier) { this.classifier = classifier }

        Question resolve(List<Question> others) { resolve(others, classifier) }
    }


}
