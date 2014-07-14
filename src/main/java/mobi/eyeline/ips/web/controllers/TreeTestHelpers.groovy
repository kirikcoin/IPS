package mobi.eyeline.ips.web.controllers

import mobi.eyeline.ips.components.tree.TreeEdge
import mobi.eyeline.ips.components.tree.TreeNode

class TreeTestHelpers {

    static TreeNode q(int id) \
      { new TreeNode(id, questionLabel(id), questionDetail(id)) }
    static void linkNodes(TreeNode self, List<TreeNode> targets) \
       { self.edges.addAll edges(self, targets) }

    static def link = { Map<Object, TreeNode> map, def source, def targets ->
        if (targets instanceof List)    linkNodes map[source], targets.collect { val -> map[val] }
        else                            linkNodes map[source], [map[targets]]
    }

    static def root = { Map map, def source -> map[source] }

    static def tree(Map map, Closure closure) \
      { closure map.withDefault(TreeTestHelpers.&q) }
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
}
