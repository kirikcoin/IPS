package mobi.eyeline.ips.web.components.input_text

import javax.faces.component.EditableValueHolder
import javax.faces.component.UIComponent
import javax.faces.context.FacesContext

/**
 * No value trimming implementation.
 */
public class InputTextRenderer extends mobi.eyeline.util.jsf.components.input_text.InputTextRenderer {

    private static String decodeString(Object value) {
        def v = value?.toString()
        return (v == null || v.empty) ? null : v
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> reqParams = context.externalContext.requestParameterMap
        String submittedValue = decodeString(reqParams.get(component.id))
        ((EditableValueHolder) component).submittedValue = [submittedValue] as String[]

        (component as InputText).search = reqParams.get("user_typed${component.id}")
    }
}
