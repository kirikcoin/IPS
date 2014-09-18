package mobi.eyeline.ips.web.controllers.settings

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.InvitationDelivery
import mobi.eyeline.ips.model.UiProfile
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.validators.ImageValidator
import mobi.eyeline.util.jsf.components.input_file.UploadedFile

import javax.faces.context.FacesContext
import javax.faces.model.SelectItem
import javax.servlet.http.HttpServletRequest

import static mobi.eyeline.ips.web.controllers.BaseController.getStrings

@SuppressWarnings('UnnecessaryQualifiedReference')
@CompileStatic
class SettingsPageController extends BaseController {

    private final UserRepository userRepository = Services.instance().userRepository

    User user
    UploadedFile imageFile
    Boolean error

    final List<SelectItem> skins = UiProfile.Skin.values().collect {
        UiProfile.Skin skin -> new SelectItem(skin.toString(), nameOf(skin))
    }

    SettingsPageController() {
        this.user = getCurrentUser()
        if (user.uiProfile == null) {
            user.uiProfile = new UiProfile(icon: new byte[10])
        }
        ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().setAttribute(user.id.toString()+"old", user)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    String nameOf(UiProfile.Skin skin) {
        //noinspection UnnecessaryQualifiedReference
        strings["settings.skin.$skin".toString()]
    }

    void save() {
        userRepository.update(user)
        // if cancel - take in session
    }

    String cancelSave() {
//        this.user = ((HttpServletRequest) FacesContext.getCurrentInstance().
//                getExternalContext().getRequest()).getSession().getAttribute(user.id.toString()) as User

//        userRepository.update(user)
        return "LOGIN"

    }

    void saveLogo() {
        user.uiProfile.icon = imageFile.inputStream.bytes
        if (validate()) {
            userRepository.update(user)
        } else {
            addErrorMessage(strings['settings.validation.logo'], 'logo')
        }
    }

    void deleteLogo() {
        user.uiProfile.icon = null
        userRepository.update(user)
    }

    boolean validate() {
        if(ImageValidator.validate(imageFile.filename)){
            error = false
            return true
        }
        error = true
        return false
    }
}
