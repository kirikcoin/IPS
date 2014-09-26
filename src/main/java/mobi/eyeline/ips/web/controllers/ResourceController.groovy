package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.Role
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

    /**
     * Skin-specific stylesheets are located under a skin-specific path.
     * Common stylesheets are referenced by skin-specific ones.
     *
     * @return Skin-specific stylesheet path part value.
     */
    @SuppressWarnings("GrMethodMayBeStatic")
    String getSkin() {
        def userPrincipal = FacesContext.currentInstance.externalContext.userPrincipal as WebUser
        if (!userPrincipal) {
            return UiProfile.Skin.default.urlPath
        }
        def user = userRepository.load(userPrincipal.id)

        switch (user.role) {
            case MANAGER:  return user.uiProfile.skin.urlPath
            case CLIENT:   return user.manager.uiProfile.skin.urlPath
            default:
                throw new IllegalArgumentException("Unsupported role: " + user.role)
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    boolean isLogoSet() {
        HttpSession session = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession()

        LogoBean logoBean = (LogoBean) session.getAttribute("logoBean");

        return logoBean.logo != null
    }
}
