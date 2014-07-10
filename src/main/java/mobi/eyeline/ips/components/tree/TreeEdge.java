package mobi.eyeline.ips.components.tree;

public class TreeEdge {

    private final String label;
    private final String description;

    private final TreeNode target;

    public TreeEdge(String label, String description, TreeNode target) {
        this.label = label;
        this.description = description;
        this.target = target;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public TreeNode getTarget() {
        return target;
    }
}
