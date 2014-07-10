package mobi.eyeline.ips.components.tree;

import mobi.eyeline.util.jsf.components.HtmlWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TreeRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);

        if (!(component instanceof Tree)) {
            return;
        }

        final Tree tree = (Tree) component;
        final HtmlWriter w = new HtmlWriter(context.getResponseWriter());

        w.a("\n<div" +
                " id='" + tree.getId() + "'" +
                " style='" + (tree.isVisible() ? "" : "display: none") + "'" +
                ">");

        w.a("\n<svg" +
                " class='eyeline_tree'" +
                " width='" + tree.getWidth() + "'" +
                " height='" + tree.getHeight() + "'" +
                ">");

        w.a("\n<g transform='translate(20,20)'/>");
        w.a("\n</svg>");
        w.a("\n</div>");

        renderJs(tree, w);
    }

    private void renderJs(Tree tree, HtmlWriter w) throws IOException {
        w.a("\n<script language=\"javascript\" type=\"text/javascript\">");

        w.a("var options = {};");
        w.a("options['graph']=").a(new JsonBuilder().toJson(tree.getValue())).a(";");

        w.a("createTree('" + tree.getId() + "', options);");

        w.a("\n</script>");
    }


    // JS directed graph structure:
    //
    //  graph = {
    //      nodes: [
    //          {
    //              id: '', label: '', description: ''
    //          },
    //          ...
    //      ],
    //      edges: [
    //          {
    //              from: '', to: '', label: '', description: ''
    //          }
    //          ...
    //      ]
    //  }
    //

    private static class JsonBuilder {

        private Map<TreeNode, Integer> nodeIds = new HashMap<>();

        public String toJson(TreeNode root) throws IOException {
            final StringBuilder w = new StringBuilder();

            final HashSet<TreeNode> nodes = new HashSet<>();
            final HashSet<OriginatingTreeEdge> edges = new HashSet<>();
            collect(root, nodes, edges);

            w.append("{nodes: [");
            for (TreeNode node : nodes) {
                nodeToJson(w, node);
                w.append(",");
            }
            w.append("], edges: [");
            for (OriginatingTreeEdge edge : edges) {
                edgeToJson(w, edge.getNode(), edge);
                w.append(",");
            }
            w.append("]}");
            return w.toString();
        }

        private void collect(TreeNode root,
                             Set<TreeNode> nodes,
                             Set<OriginatingTreeEdge> edges) {
            if (nodes.add(root)) {
                for (TreeEdge edge : root.getEdges()) {
                    edges.add(new OriginatingTreeEdge(root, edge));
                    collect(edge.getTarget(), nodes, edges);
                }
            }
        }

        private String getId(TreeNode node) {
            if (!nodeIds.containsKey(node)) {
                final int id = nodeIds.isEmpty() ? 0 : Collections.max(nodeIds.values()) + 1;
                nodeIds.put(node, id);
            }

            return String.valueOf(nodeIds.get(node));
        }

        private void nodeToJson(Appendable w,
                                TreeNode node) throws IOException {
            w.append("{");
            w.append("id: '").append(getId(node)).append("'");
            w.append(", ");
            w.append("label: '").append(escapeJs(node.getLabel())).append("'");
            if (node.getDescription() != null) {
                w.append(", ");
                w.append("description: '").append(escapeJs(node.getDescription())).append("'");
            }
            w.append("}");
        }

        private void edgeToJson(Appendable w,
                                TreeNode from,
                                TreeEdge edge) throws IOException {
            w.append("{");
            w.append("from: '").append(getId(from)).append("'");
            w.append(", ");
            w.append("to: '").append(getId(edge.getTarget())).append("'");
            w.append(", ");
            w.append("label: '").append(escapeJs(edge.getLabel())).append("'");
            if (edge.getDescription() != null) {
                w.append(", ");
                w.append("description: '").append(escapeJs(edge.getDescription())).append("'");
            }
            w.append("}");
        }

        private String escapeJs(String value) {
            value = value.replaceAll("\n", "\\\\n'");
            value = value.replaceAll("\\\\", "\\\\\\\\");
            value = value.replaceAll("'", "\\\\'");
            return value;
        }

        private static class OriginatingTreeEdge extends TreeEdge {
            private final TreeNode node;

            private OriginatingTreeEdge(TreeNode node, TreeEdge edge) {
                super(edge.getLabel(), edge.getDescription(), edge.getTarget());
                this.node = node;
            }

            public TreeNode getNode() {
                return node;
            }
        }
    }

}
