package mobi.eyeline.ips.components.tree;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

public class Tree extends UIOutput {
    public String getFamily() {
        return "Eyeline";
    }

    protected Renderer getRenderer(FacesContext facesContext) {
        return new TreeRenderer();
    }

    public String getRendererType() {
        return "mobi.eyeline.ips.components.tree";
    }
}
