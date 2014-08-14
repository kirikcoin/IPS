package mobi.eyeline.ips.components.tree;

import mobi.eyeline.util.jsf.components.HtmlWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * The component depends on:
 * <ol>
 *     <li>tree.js</li>
 *     <li>tree.less</li>
 *     <li>d3.js and dagre-d3.js</li>
 *     <li>FontAwesome stylesheets for toolbar icons</li>
 * </ol>
 */
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
                " class='eyeline_tree_wrapper'" +
                ">");

        renderToolbar(tree, w);

        w.a("\n<svg" +
                " class='eyeline_tree'" +
                " width='" + tree.getWidth() + "'" +
                " height='" + tree.getHeight() + "'" +
                ">");

        w.a("\n<g transform='translate(0, 0)'/>");

        w.a("\n</svg>");
        w.a("\n</div>");

        renderJs(tree, w);
    }

    private void renderToolbar(Tree tree, HtmlWriter w) throws IOException {
        w.a("\n<div" +
                " class='eyeline_tree_toolbar'" +
                " style='" +
                "position: absolute; padding-top: 10px; margin-left: " + (tree.getWidth() - 30) + "px;" +
                "'" +
                ">");

        w.a("\n<span" +
                " class='zoom_in fa fa-search-plus fa-2x'" +
                ">");
        w.a("\n</span>");

        w.a("\n<span" +
                " class='zoom_out fa fa-search-minus fa-2x'" +
                "/>");
        w.a("\n</span>");

        w.a("\n<span" +
                " class='zoom_reset fa fa-arrows-alt fa-2x'" +
                "/>");
        w.a("\n</span>");

        w.a("\n</div>");
    }

    private void renderJs(Tree tree, HtmlWriter w) throws IOException {
        w.a("\n<script language=\"javascript\" type=\"text/javascript\">");

        w.a("var options = {};");
        w.a("options['graph']=").a(new JsonBuilder().toJson(tree.getValue())).a(";");
        w.a("options['direction']='").a(tree.getDirection().name()).a("';");

        w.a("createTree('" + tree.getId() + "', options);");

        w.a("\n</script>");
    }


    // JS directed graph structure:
    //
    //  graph = {
    //      nodes: [
    //          {
    //              id: '', label: '', description: '', styleClass: ''
    //          },
    //          ...
    //      ],
    //      edges: [
    //          {
    //              from: '', to: '', label: '', description: '', styleClass: ''
    //          }
    //          ...
    //      ]
    //  }
    //

    private static class JsonBuilder {

        public String toJson(TreeNode root) throws IOException {
            final StringBuilder w = new StringBuilder();

            final Set<TreeNode> nodes = new TreeSet<>();
            final Set<OriginatingTreeEdge> edges = new TreeSet<>(OriginatingTreeEdge.COMPARATOR);
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

        private void nodeToJson(Appendable w,
                                TreeNode node) throws IOException {
            w.append("{");
            w.append("id: '").append(String.valueOf(node.getId())).append("'");
            w.append(", ");
            w.append("label: '").append(escapeJs(node.getLabel())).append("'");
            if (node.getDescription() != null) {
                w.append(", ");
                w.append("description: '").append(escapeJs(node.getDescription())).append("'");
            }
            if (node.getStyleClass() != null) {
                w.append(", ");
                w.append("styleClass: '").append(escapeJs(node.getStyleClass())).append("'");
            }
            w.append("}");
        }

        private void edgeToJson(Appendable w,
                                TreeNode from,
                                TreeEdge edge) throws IOException {
            w.append("{");
            w.append("from: '").append(String.valueOf(from.getId())).append("'");
            w.append(", ");
            w.append("to: '").append(String.valueOf(edge.getTarget().getId())).append("'");
            if (edge.getLabel() != null) {
                w.append(", ");
                w.append("label: '").append(escapeJs(edge.getLabel())).append("'");
            }
            if (edge.getDescription() != null) {
                w.append(", ");
                w.append("description: '").append(escapeJs(edge.getDescription())).append("'");
            }
            if (edge.getStyleClass() != null) {
                w.append(", ");
                w.append("styleClass: '").append(escapeJs(edge.getStyleClass())).append("'");
            }
            w.append("}");
        }

        private String escapeJs(String value) {
            value = value.replaceAll("\n", "\\\\n");
            value = value.replaceAll("\\\\", "\\\\\\\\");
            value = value.replaceAll("'", "\\\\'");
            return value;
        }

        private static class OriginatingTreeEdge extends TreeEdge {
            private final TreeNode node;

            private OriginatingTreeEdge(TreeNode node, TreeEdge edge) {
                super(edge.getId(), edge.getLabel(), edge.getDescription(), edge.getStyleClass(), edge.getTarget());
                this.node = node;
            }

            public TreeNode getNode() {
                return node;
            }

            public static final Comparator<OriginatingTreeEdge> COMPARATOR =
                    new Comparator<OriginatingTreeEdge>() {
                        @Override
                        public int compare(OriginatingTreeEdge l, OriginatingTreeEdge r) {
                            return (l.getNode().equals(r.getNode())) ?
                                    l.compareTo(r) : l.getNode().compareTo(r.getNode());
                        }
                    };
        }
    }

}
