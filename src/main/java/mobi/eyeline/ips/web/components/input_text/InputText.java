package mobi.eyeline.ips.web.components.input_text;

import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

public class InputText extends mobi.eyeline.util.jsf.components.input_text.InputText {
    @Override
    protected Renderer getRenderer(FacesContext context) {
        return new InputTextRenderer();
    }
}
