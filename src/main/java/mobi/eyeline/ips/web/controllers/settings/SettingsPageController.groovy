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


import javax.faces.model.SelectItem

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
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    String nameOf(UiProfile.Skin skin) {
        //noinspection UnnecessaryQualifiedReference
        strings["settings.skin.$skin".toString()]
    }

    void save() {
        user.uiProfile.icon = imageFile.inputStream.bytes
        if (validate()) {
            userRepository.update(user)
        } else{
            addErrorMessage(strings['settings.validation.logo'], 'logo')
        }


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
