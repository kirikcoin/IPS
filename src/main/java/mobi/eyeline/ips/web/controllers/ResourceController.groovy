package mobi.eyeline.ips.web.controllers

import javax.faces.context.FacesContext

import static mobi.eyeline.ips.web.BuildVersion.BUILD_VERSION

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
}
