package mobi.eyeline.ips.utils

import mobi.eyeline.ips.components.tree.TreeEdge
import mobi.eyeline.ips.components.tree.TreeNode

class TreeBuilder {
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

    static TreeNode tree(Map map, @DelegatesTo(MapDelegate) Closure closure) \
      { new MapDelegate(map.withDefault(TreeBuilder.&q)).with closure }
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

    private static List describeEdges(TreeNode node, Set processed = []) {
        !processed.add(node) ? [] : [
                node.edges.collect { "[$node.id] --$it.id--> [$it.target.id]\n" },
                '\n',
                node.edges.collect { it.target }.collect { describeEdges(it, processed) }
        ].flatten()
    }

    static void init() {
        //noinspection GrUnresolvedAccess
        TreeNode.metaClass.describe = {
            ["Root: [$delegate.id]\n\n", * describeEdges(delegate as TreeNode)].join('').trim()
        }
    }
    static { init() }
}
