package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.UiProfile
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.auth.WebUser

import javax.faces.context.FacesContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

import static mobi.eyeline.ips.model.Role.CLIENT
import static mobi.eyeline.ips.model.Role.MANAGER
import static mobi.eyeline.ips.web.BuildVersion.BUILD_VERSION

@CompileStatic
class ResourceController implements Serializable {

    private final UserRepository userRepository = Services.instance().userRepository

    /**
     * @return Current request context path.
     */
    @SuppressWarnings("GrMethodMayBeStatic")
    String getPath() {
        FacesContext.currentInstance.externalContext.requestContextPath
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    String getVersion() { BUILD_VERSION }

    @SuppressWarnings("GrMethodMayBeStatic")
    String getSkin() {
        if (currentUiProfile == null) {
            return UiProfile.Skin.default.urlPath
        } else {
            return currentUiProfile.skin.urlPath
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    boolean isLogoSet() {
        return (currentUiProfile?.icon != null)
    }

    private UiProfile getCurrentUiProfile() {
        def userPrincipal = FacesContext.currentInstance.externalContext.userPrincipal as WebUser
        if (!userPrincipal) {
            return null
        }

        def user = userRepository.load(userPrincipal.id)

        switch (user.role) {
            case MANAGER:  return user.uiProfile
            case CLIENT:   return user.manager.uiProfile
            default:
                throw new IllegalArgumentException("Unsupported role: " + user.role)
        }
    }
}
