package mobi.eyeline.ips.components.tree;

public class TreeEdge implements Comparable<TreeEdge> {

    private final int id;
    private final String label;
    private final String description;
    private final String styleClass;

    private final TreeNode target;

    public TreeEdge(int id,
                    String label,
                    String description,
                    String styleClass,
                    TreeNode target) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.styleClass = styleClass;
        this.target = target;
    }

    public TreeEdge(int id,
                    String label,
                    String description,
                    TreeNode target) {
        this(id, label, description, null, target);
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

    public String getStyleClass() {
        return styleClass;
    }

    public TreeNode getTarget() {
        return target;
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") TreeEdge that) {
        return Integer.compare(this.getId(), that.getId());
    }

    @Override
    public String toString() {
        return "TreeEdge{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", target=" + target +
                '}';
    }

}
