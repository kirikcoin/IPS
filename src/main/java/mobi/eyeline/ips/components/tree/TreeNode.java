package mobi.eyeline.ips.components.tree;

import java.util.LinkedList;
import java.util.List;

public class TreeNode implements Comparable<TreeNode> {

    private final int id;
    private final String label;
    private final String description;

    private final String styleClass;

    /**
     * Edges originating from this node.
     */
    private final List<TreeEdge> edges = new LinkedList<>();

    public TreeNode(int id, String label, String description, String styleClass) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.styleClass = styleClass;
    }

    public TreeNode(int id, String label, String description) {
        this(id, label, description, null);
    }

    public int getId() {
        return id;
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

    public String getStyleClass() {
        return styleClass;
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") TreeNode that) {
        return Integer.compare(this.getId(), that.getId());
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }
}
