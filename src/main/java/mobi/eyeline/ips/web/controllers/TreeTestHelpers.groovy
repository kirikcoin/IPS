package mobi.eyeline.ips.web.controllers

import mobi.eyeline.ips.components.tree.TreeEdge
import mobi.eyeline.ips.components.tree.TreeNode

class TreeTestHelpers {

    // Note: this should be deleted once hierarchical surveys are implemented.
    // The following code should probably be used in tests, so is duplicated in GraphUtilTest.

    static TreeNode q(int id) \
      { new TreeNode(id, questionLabel(id), questionDetail(id)) }
    static void linkNodes(TreeNode self, List<TreeNode> targets) \
       { self.edges.addAll edges(self, targets) }

    static def tree(Map map, Closure closure) \
      { new MapDelegate(map.withDefault(TreeTestHelpers.&q)).with closure }
    static private List<TreeEdge> edges(TreeNode from, Collection<TreeNode> targets) \
       { def idx = 0; targets.collect { TreeNode to -> a(++idx, from, to) } }
    static private String questionLabel(int qid) \
      { "Вопрос $qid" }
    static private String questionDetail(int qid) \
      { "Текст вопроса #$qid, возможно очень и очень длинный" }
    static private String answerDetail(int aid, TreeNode q) \
      { "Длинный текст варианта #$aid на вопрос '$q.label' " }
    static private TreeEdge a(int id, TreeNode from, TreeNode to) \
      { new TreeEdge(id, "$id", answerDetail(id, from), to) }

    static class MapDelegate {
        private final Map map

        MapDelegate(Map map) { this.map = map }

        @SuppressWarnings("GroovyAssignabilityCheck")
        def link(def source, def targets) {
            if (targets instanceof List)    linkNodes map[source], targets.collect { val -> map[val] }
            else                            linkNodes map[source], [map[targets]]
        }

        def root (def source) { map[source] }
    }
}
