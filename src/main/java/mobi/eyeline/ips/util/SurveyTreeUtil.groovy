package mobi.eyeline.ips.util

import com.j256.simplejmx.common.JmxOperation
import com.j256.simplejmx.common.JmxOperationInfo
import com.j256.simplejmx.common.JmxResource
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.components.tree.TreeEdge
import mobi.eyeline.ips.components.tree.TreeNode
import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.service.Services


@CompileStatic
@Slf4j('logger')
@JmxResource(domainName = "mobi.eyeline.ips")
class SurveyTreeUtil {

    private static TreeNode addQuestion(Question q,
                                        TreeNode terminal,
                                        Map<Integer, TreeNode> target = new LinkedHashMap<>()) {
        new TreeNode(q.id, "${q.activeIndex + 1}. $q.title" as String, q.title).with { TreeNode n ->
            if (!target.put(q.id, n)) {
                n.edges.addAll(q.activeOptions.collect { QuestionOption opt -> new TreeEdge(
                        opt.id,
                        opt.activeIndex + 1 as String,
                        opt.answer,
                        opt.nextQuestion ?
                                addQuestion(opt.nextQuestion, terminal, target) :
                                terminal)
                })
            }
            n
        }
    }

    private static Collection<TreeNode> listNodes(TreeNode from,
                                                  Map<Integer, TreeNode> target = new LinkedHashMap<>()) {
        if (!target.put(from.id, from)) from.edges.each { TreeEdge e -> listNodes(e.target, target) }
        target.values()
    }

    private static Collection<TreeEdge> listEdges(TreeNode from) {
        listNodes(from).collect { TreeNode n -> n.edges}.flatten()
    }

    static TreeNode asTree(Survey survey,
                           String terminalLabel,
                           String terminalDescription,
                           String unusedLabel) {

        if (survey.firstQuestion) {
            def terminal = new TreeNode(-1, terminalLabel, terminalDescription)
            TreeNode tree = addQuestion(survey.firstQuestion, terminal)

            int maxEdgeId = listEdges(tree).max { TreeEdge e -> e.id }.id

            // Add nodes for unreachable questions by linking from the `terminal'
            listNodes(tree).with { Collection<TreeNode> nodes ->
                terminal.edges.addAll \
             survey.activeQuestions
                        .grep { Question q -> !nodes.any { TreeNode n -> n.id == q.id } }
                        .collect { Question q ->
                    def title = "${q.activeIndex + 1}. $q.title ($unusedLabel)"
                    new TreeEdge(++maxEdgeId, null, null, 'treeInvisible',
                            new TreeNode(q.id, title as String, q.title, 'treeUnused')) }
            }

            return tree

        } else {
            return null
        }
    }

    // TODO: delete after migration.
    @SuppressWarnings(["GrMethodMayBeStatic", "GroovyUnusedDeclaration"])
    @JmxOperation(operationAction = JmxOperationInfo.OperationAction.ACTION)
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
