package mobi.eyeline.ips.util

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.components.tree.TreeEdge
import mobi.eyeline.ips.components.tree.TreeNode
import mobi.eyeline.ips.model.Page
import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.Survey

@CompileStatic
@Slf4j('logger')
class SurveyTreeUtil {

  private static TreeNode addQuestion(Page p,
                                      TreeNode terminal,
                                      String defaultLabel,
                                      String defaultDescription,
                                      String questionPrefix,
                                      String extLinkPrefix,
                                      Map<Integer, TreeNode> target = new LinkedHashMap<>()) {
    final String prefix = (p instanceof Question) ? questionPrefix : extLinkPrefix
    final String style = (p instanceof Question) ? null : 'ext-link'
    new TreeNode(p.id, "$prefix ${p.activeIndex + 1}. $p.title" as String, p.title, style).with { node ->
      if (!target.containsKey(node.id)) {
        target.put(node.id, node)

        // id of default question edge should be not equal to normal edge id,
        // for example <0 and equals to question id +1
        if (p instanceof Question) {
          if (p.enabledDefaultAnswer) {
            if (p.defaultPage == null) {
              node.edges << new TreeEdge(-(p.id + 1),
                  defaultLabel,
                  defaultDescription,
                  terminal)
            } else {
              node.edges << new TreeEdge(-(p.id + 1),
                  defaultLabel,
                  defaultDescription,
                  addQuestion(p.defaultPage, terminal, defaultLabel, defaultDescription, questionPrefix, extLinkPrefix, target))
            }
          }
          node.edges.addAll(p.activeOptions.collect { opt ->
            new TreeEdge(
                opt.id,
                opt.activeIndex + 1 as String,
                "${opt.activeIndex + 1}. $opt.answer" as String,
                opt.nextPage ?
                    addQuestion(opt.nextPage, terminal, defaultLabel, defaultDescription, questionPrefix, extLinkPrefix, target) :
                    terminal)
          })
        }
        node
      } else {
        target[p.id]
      }
    }
  }

  private static Collection<TreeNode> listNodes(TreeNode from,
                                                Map<Integer, TreeNode> target = new LinkedHashMap<>()) {
    if (!target.put(from.id, from)) from.edges.each { e -> listNodes(e.target, target) }
    target.values()
  }

  private static Collection<TreeEdge> listEdges(TreeNode from) {
    listNodes(from).collect { it.edges }.flatten()
  }

  static TreeNode asTree(Survey survey,
                         String terminalLabel,
                         String terminalDescription,
                         String unusedLabel,
                         String defaultLabel,
                         String defaultDescription,
                         String questionPrefix,
                         String extLinkPrefix) {

    if (survey.firstQuestion) {
      def terminal = new TreeNode(-1, terminalLabel, terminalDescription)
      TreeNode tree = addQuestion(
          survey.firstQuestion, terminal, defaultLabel, defaultDescription, questionPrefix, extLinkPrefix)

      int maxEdgeId = listEdges(tree).max { TreeEdge e -> e.id }.id

      // Add nodes for unreachable questions by linking from the `terminal'
      listNodes(tree).with { nodes ->
        terminal.edges.addAll  \
              survey.activePages
            .grep { Page p -> !nodes.any { TreeNode n -> n.id == p.id } }
            .collect { p ->
          final String prefix = (p instanceof Question) ? questionPrefix : extLinkPrefix
          def title = "$prefix ${p.activeIndex + 1}. $p.title ($unusedLabel)"
          new TreeEdge(++maxEdgeId, null, null, 'treeInvisible',
              new TreeNode(p.id, title as String, p.title, 'treeUnused'))
        }
      }

      return tree

    } else {
      return null
    }
  }
}
