package mobi.eyeline.ips.components.tree;

import java.util.LinkedList;
import java.util.List;

public class TreeNode {

    private final String label;
    private final String description;

    /**
     * Edges originating from this node.
     */
    private final List<TreeEdge> edges = new LinkedList<>();

    public TreeNode(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public List<TreeEdge> getEdges() {
        return edges;
    }
}
