package mobi.eyeline.ips.util

import com.j256.simplejmx.common.JmxOperation
import com.j256.simplejmx.common.JmxResource
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.components.tree.TreeEdge
import mobi.eyeline.ips.components.tree.TreeNode
import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.service.Services

import static com.j256.simplejmx.common.JmxOperationInfo.OperationAction.ACTION

@CompileStatic
@Slf4j('logger')
@JmxResource(domainName = "mobi.eyeline.ips")
class SurveyTreeUtil {

    private static TreeNode addQuestion(Map<Integer, TreeNode> target,
                                        Question q,
                                        TreeNode terminal) {
        new TreeNode(q.id, "${q.activeIndex + 1}. $q.title" as String, q.title).with { TreeNode n ->
            if (!target.put(q.id, n)) {
                n.edges.addAll(q.activeOptions.collect { QuestionOption opt -> new TreeEdge(
                        opt.id,
                        opt.activeIndex + 1 as String,
                        opt.answer,
                        opt.nextQuestion ?
                                addQuestion(target, opt.nextQuestion, terminal) :
                                terminal)
                })
            }
            n
        }
    }

    static TreeNode asTree(Survey survey,
                           String terminalLabel,
                           String terminalDescription) {
        survey.firstQuestion ? addQuestion(
                [:], survey.firstQuestion, new TreeNode(-1, terminalLabel, terminalDescription)) : null
    }

    // TODO: delete after migration.
    @SuppressWarnings(["GrMethodMayBeStatic", "GroovyUnusedDeclaration"])
    @JmxOperation(operationAction = ACTION)
    void migrateToTreeStructure() {
        logger.info 'Migrating to tree survey structure'

        def surveyRepository = Services.instance().surveyRepository
        def questionOptionRepository = Services.instance().questionOptionRepository

        surveyRepository.list()
                .grep { Survey s -> s.active }
                .each { Survey s ->
            logger.info "Processing sid = [$s.id]"
            try {
                s.activeQuestions.each { Question q ->
                    logger.info "Processing qid = [$q.id], next qid = [${q.next?.id}]"
                    q.activeOptions.each { QuestionOption opt ->
                        opt.nextQuestion = q.next
                        questionOptionRepository.update opt
                    }
                }
            } catch (Exception e) {
                logger.error "Migration failed, sid = [$s.id]", e
            }
        }

        logger.info 'Migration finished'
    }
}
