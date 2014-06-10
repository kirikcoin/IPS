package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic
import mobi.eyeline.ips.service.Services

import javax.faces.context.FacesContext

import static mobi.eyeline.ips.web.BuildVersion.BUILD_VERSION

@CompileStatic
class ResourceController implements Serializable {

    /**
     * @return Current request context path.
     */
    @SuppressWarnings("GrMethodMayBeStatic")
    String getPath() {
        FacesContext.currentInstance.externalContext.requestContextPath
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    String getVersion() { BUILD_VERSION }

    /**
     * Skin-specific stylesheets are located under a skin-specific path.
     * Common stylesheets are referenced by skin-specific ones.
     *
     * @return Skin-specific stylesheet path part value.
     */
    @SuppressWarnings("GrMethodMayBeStatic")
    String getSkin() { Services.instance().locationService.skin }
}
