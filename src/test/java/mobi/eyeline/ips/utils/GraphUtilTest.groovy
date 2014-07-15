package mobi.eyeline.ips.utils

import mobi.eyeline.ips.components.tree.TreeEdge
import mobi.eyeline.ips.components.tree.TreeNode
import mobi.eyeline.ips.util.GraphUtil

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize

class GraphUtilTest extends GroovyTestCase {

    static TreeNode q(int id) {
        return new TreeNode(id, "${id}", null) {
            @Override
            boolean equals(Object obj) {
                (obj instanceof TreeNode) && id == obj.id
            }
        }
    }

    static void linkNodes(TreeNode self, List<TreeNode> targets) \
       { self.edges.addAll edges(self, targets) }

    static def tree(Map map, Closure closure) \
      { new MapDelegate(map.withDefault(GraphUtilTest.&q)).with closure }
    static private List<TreeEdge> edges(TreeNode from, Collection<TreeNode> targets) \
       { def idx = 0; targets.collect { TreeNode to -> a(++idx, from, to) } }

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

    void test1() {

        def tree = tree([
                end: new TreeNode(0, 'Конец опроса', null),
                u1: new TreeNode(10, 'u1', null),
                u2: new TreeNode(20, 'u2', null)
        ]) {
            link 1, [2, 3, 0]
            link 2, [4]*3
            link 3, [5]*3
            link 'u1', [5]*3
            link 'u2', [1, 2]
            link 4, ['end']*3
            link 5, ['end']*3
            root 1
        }

        assertThat GraphUtil.findCycles(tree), hasSize(0)
        assertEquals(
                GraphUtil.findUnreachable(tree, [tree, q(10), q(20)]).collect {it.label}, ['10', '20'])
    }

    void test2() {

        def tree = tree([
                end: new TreeNode(0, 'Конец опроса', null),
        ]) {
            link 1, [2, 3, 0]
            link 2, [0]
            link 0, 2
            root 2
        }

        assertEquals([[q(2), q(0)]], GraphUtil.findCycles(tree))
    }

    void test3() {

        def tree = tree([
                end: new TreeNode(0, 'Конец опроса', null),
        ]) {
            link 1, [2, 3, 0]
            link 2, [0]
            link 0, [1, 3]
            link 3, [1]
            root 2
        }

        assertEquals([
                [q(2), q(0), q(1)],
                [q(2), q(0), q(3), q(1)],
                [q(0), q(1)],
                [q(0), q(3), q(1)],
                [q(1), q(3)]],
                GraphUtil.findCycles(tree))
    }
}
