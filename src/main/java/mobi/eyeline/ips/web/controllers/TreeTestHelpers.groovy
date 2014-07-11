package mobi.eyeline.ips.web.controllers

import mobi.eyeline.ips.components.tree.TreeEdge
import mobi.eyeline.ips.components.tree.TreeNode

@SuppressWarnings("GrMethodMayBeStatic")
class TreeTestHelpers {

    TreeNode q(int id) \
      { new TreeNode(id, questionLabel(id), questionDetail(id)) }
    void link(TreeNode self, List<TreeNode> targets) \
       { self.edges.addAll edges(self, targets) }

    private List<TreeEdge> edges(TreeNode from, Collection<TreeNode> targets) \
       { def idx = 0; targets.collect { TreeNode to -> a(++idx, from, to) } }
    private String questionLabel(int qid) \
      { "Вопрос $qid" }
    private String questionDetail(int qid) \
      { "Текст вопроса #$qid, возможно очень и очень длинный" }
    private String answerDetail(int aid, TreeNode q) \
      { "Длинный текст варианта #$aid на вопрос '$q.label' " }
    private TreeEdge a(int id, TreeNode from, TreeNode to) \
      { new TreeEdge(id, "$id", answerDetail(id, from), to) }
}
