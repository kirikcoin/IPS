package mobi.eyeline.ips.components.tree;


import mobi.eyeline.util.jsf.components.utils.base.UIPanelImpl;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;

@FacesComponent("mobi.eyeline.ips.Tree")
public class Tree extends UIPanelImpl {

  private int height;
  private int width;

  private TreeDirection direction = TreeDirection.LR;

  private TreeNode value;

  public int getHeight() {
    return valueExpression("height", height);
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return valueExpression("height", width);
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public TreeDirection getDirection() {
    return valueExpression("direction", direction);
  }

  public void setDirection(TreeDirection direction) {
    this.direction = direction;
  }

  public TreeNode getValue() {
    return valueExpression("value", value);
  }

  public void setValue(TreeNode value) {
    this.value = value;
  }

  @Override
  public Object saveState(FacesContext context) {
    return new Object[]{
        super.saveState(context),
        height,
        width,
        direction,
        value
    };
  }

  @Override
  public void restoreState(FacesContext context, Object state) {
    final Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    height = (Integer) values[1];
    width = (Integer) values[2];
    direction = (TreeDirection) values[3];
    value = (TreeNode) values[4];
  }
}
