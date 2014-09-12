package mobi.eyeline.ips.components.tree;


import mobi.eyeline.utils.HtmlWriter;
import mobi.eyeline.utils.base.RendererImpl;

import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import static mobi.eyeline.utils.HtmlWriter.AttributeUtils.inCase;
import static mobi.eyeline.utils.HtmlWriter.CommonAttributes.DISPLAY_NONE;
import static mobi.eyeline.utils.HtmlWriter.Tag.DIV;
import static mobi.eyeline.utils.HtmlWriter.Tag.SPAN;
import static mobi.eyeline.utils.base.Components.COMPONENT_FAMILY;
import static mobi.eyeline.utils.base.Components.getParentCustomComponentId;

/**
 * The component depends on:
 * <ol>
 *     <li>tree.js</li>
 *     <li>tree.less</li>
 *     <li>d3.js and dagre-d3.js</li>
 *     <li>FontAwesome stylesheets for toolbar icons</li>
 * </ol>
 */
@FacesRenderer(
        componentFamily = COMPONENT_FAMILY,
        rendererType = "mobi.eyeline.ips.TreeRenderer")
public class TreeRenderer extends RendererImpl<Tree> {

    @Override
    protected void encodeBegin(FacesContext context,
                               HtmlWriter w,
                               Tree tree) throws IOException {

        w.begin(DIV,
                "id", tree.getId(),
                inCase(!tree.isVisible(), DISPLAY_NONE),
                "class", "eyeline_tree_wrapper");

        renderToolbar(tree, w);

        // TODO: add SVG tag to HtmlWriter and replace with proper tag creation.
        w.a("\n<svg" +
                " class='eyeline_tree'" +
                " width='" + tree.getWidth() + "'" +
                " height='" + tree.getHeight() + "'" +
                ">");

        w.a("\n<g transform='translate(0, 0)'/>");

        w.a("\n</svg>");
        w.end(DIV);

        renderJs(tree, w);
    }

    private void renderToolbar(Tree tree, HtmlWriter w) throws IOException {
        w.begin(DIV,
                "class", "eyeline_tree_toolbar",
                "style", "position: absolute;" +
                        " padding-top: 10px;" +
                        " margin-left: " + (tree.getWidth() - 30) + "px;");

        w.tag(SPAN, "class", "zoom_in fa fa-search-plus fa-2x");
        w.tag(SPAN, "class", "zoom_out fa fa-search-minus fa-2x");
        w.tag(SPAN, "class", "zoom_reset fa fa-arrows-alt fa-2x");

        w.end(DIV);
    }

    private void renderJs(Tree tree, HtmlWriter w) throws IOException {
        w.a("\n<script language=\"javascript\" type=\"text/javascript\">");

        w.a("var options = {};");
        w.a("options['graph']=").a(new JsonBuilder().toJson(tree.getValue())).a(";");
        w.a("options['direction']='").a(tree.getDirection().name()).a("';");

        w.a("createTree('" + tree.getId() + "', '" + getParentCustomComponentId(tree) + "', options);");

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
                edgeToJson(w, edge.getOrigin(), edge);
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
            private final TreeNode origin;

            private OriginatingTreeEdge(TreeNode origin, TreeEdge edge) {
                super(edge);
                this.origin = origin;
            }

            public TreeNode getOrigin() {
                return origin;
            }

            public static final Comparator<OriginatingTreeEdge> COMPARATOR =
                    new Comparator<OriginatingTreeEdge>() {
                        @Override
                        public int compare(OriginatingTreeEdge l, OriginatingTreeEdge r) {
                            return (l.getOrigin().equals(r.getOrigin())) ?
                                    l.compareTo(r) : l.getOrigin().compareTo(r.getOrigin());
                        }
                    };
        }
    }

}
