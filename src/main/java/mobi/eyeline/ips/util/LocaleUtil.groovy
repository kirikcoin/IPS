package mobi.eyeline.ips.util

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j

import javax.faces.context.FacesContext

@Log4j
@CompileStatic
class LocaleUtil {

    static String getExportCharset() {
        try {
            final lang = FacesContext.currentInstance.viewRoot.locale.language
            return lang == 'ru' ? 'cp1251' : 'UTF-8'

        } catch (Exception e) {
            log.error 'Error determining export charset', e
            return 'UTF-8'
        }
    }

}
