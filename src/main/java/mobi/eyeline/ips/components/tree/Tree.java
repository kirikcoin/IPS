package mobi.eyeline.ips.components.tree;

import mobi.eyeline.util.jsf.components.base.UIPanelImpl;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

public class Tree extends UIPanelImpl {

    private int height;
    private int width;

    private TreeNode value;


    protected Renderer getRenderer(FacesContext facesContext) {
        return new TreeRenderer();
    }

    public String getRendererType() {
        return "mobi.eyeline.ips.components.tree";
    }

    public int getHeight() {
        final ValueExpression exp = getValueExpression("height");
        if (exp == null) {
            return height;
        } else {
            return (Integer) exp.getValue(getFacesContext().getELContext());
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        final ValueExpression exp = getValueExpression("width");
        if (exp == null) {
            return width;
        } else {
            return (Integer) exp.getValue(getFacesContext().getELContext());
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public TreeNode getValue() {
        final ValueExpression exp = getValueExpression("value");
        if (exp == null) {
            return value;
        } else {
            return (TreeNode) exp.getValue(getFacesContext().getELContext());
        }
    }

    public void setValue(TreeNode value) {
        this.value = value;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[] {
                super.saveState(context),
                height,
                width,
                value
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        final Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        height = (Integer) values[1];
        width = (Integer) values[2];
        value = (TreeNode) values[3];
    }
}
