package mobi.eyeline.ips.web.components.input_text;

import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

// TODO: Workaround for `jsfcomponents-38'. Remove once the issue is fixed.
public class InputText extends mobi.eyeline.util.jsf.components.input_text.InputText {
    @Override
    protected Renderer getRenderer(FacesContext context) {
        return new InputTextRenderer();
    }
}
