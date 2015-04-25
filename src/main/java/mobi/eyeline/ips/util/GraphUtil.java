package mobi.eyeline.ips.util;


import mobi.eyeline.ips.components.tree.TreeEdge;
import mobi.eyeline.ips.components.tree.TreeNode;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GraphUtil {

  /**
   * @return Empty collection if no cycles found.
   */
  public static Collection<List<TreeNode>> findCycles(TreeNode root) {
    final DirectedMultigraph<TreeNode, TreeEdge> g = new GraphWrapper(root);
    return new TarjanSimpleCycles<>(g).findSimpleCycles();
  }

  /**
   * @return Nodes unreachable from the {@code root} one. Empty collection if none found.
   */
  public static Collection<TreeNode> findUnreachable(TreeNode root,
                                                     Collection<TreeNode> nodes) {

    if (!nodes.contains(root)) {
      throw new IllegalArgumentException();
    }

    final DirectedMultigraph<TreeNode, TreeEdge> g = new GraphWrapper(nodes);
    final Collection<TreeNode> unreachable = new ArrayList<>();

    for (TreeNode node : g.vertexSet()) {
      if (DijkstraShortestPath.findPathBetween(g, root, node) == null) {
        unreachable.add(node);
      }
    }

    return unreachable;
  }

  private static class GraphWrapper extends DirectedMultigraph<TreeNode, TreeEdge> {

    public GraphWrapper(TreeNode root) {
      super(TreeEdge.class);
      addNode(root);
    }

    public GraphWrapper(Collection<TreeNode> nodes) {
      super(TreeEdge.class);

      for (TreeNode node : nodes) {
        addNode(node);
      }
    }

    private void addNode(TreeNode root) {
      if (!containsVertex(root)) {
        addVertex(root);
        for (TreeEdge edge : root.getEdges()) {
          addNode(edge.getTarget());
          addEdge(root, edge.getTarget(), edge);
        }
      }
    }

  }
}
