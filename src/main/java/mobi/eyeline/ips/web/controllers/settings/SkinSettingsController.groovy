package mobi.eyeline.ips.web.controllers.settings

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.UiProfile
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.controllers.LogoBean
import mobi.eyeline.ips.web.validators.ImageValidator
import mobi.eyeline.util.jsf.components.input_file.UploadedFile

import javax.annotation.PostConstruct
import javax.faces.model.SelectItem

@SuppressWarnings('UnnecessaryQualifiedReference')
@CompileStatic
class SkinSettingsController extends BaseController {

    private final UserRepository userRepository = Services.instance().userRepository

    User user
    UploadedFile imageFile
    Boolean error

    /** Stored in session */
    LogoBean previewLogo

    LogoBean viewSavedLogo

    final List<SelectItem> skins = UiProfile.Skin.values().collect {
        UiProfile.Skin skin -> new SelectItem(skin, nameOf(skin))
    }

    SkinSettingsController() {
        user = getCurrentUser()
    }

    @PostConstruct
    void initBeans() {
        if (!viewSavedLogo?.bytes) {
            viewSavedLogo = new LogoBean(bytes: user.uiProfile.icon)
        }

        previewLogo.bytes = viewSavedLogo.bytes
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    String nameOf(UiProfile.Skin skin) {
        //noinspection UnnecessaryQualifiedReference
        strings["settings.skin.$skin".toString()]
    }

    void save() {
        user.uiProfile.icon = viewSavedLogo.bytes

        userRepository.update(user)

        previewLogo.bytes = viewSavedLogo.bytes
        error = false
    }

    String cancelSave() {
//        viewSavedLogo = new LogoBean(bytes: user.uiProfile.icon)
        'LOGIN'
    }

    void uploadLogo() {
        if (validate()) {
            viewSavedLogo = new LogoBean(bytes: imageFile.inputStream.bytes)
            previewLogo.bytes = viewSavedLogo.bytes

        } else {
            addErrorMessage(strings['settings.validation.logo'], 'logo')
        }
    }

    void deleteLogo() {
        viewSavedLogo = new LogoBean(bytes: null as byte[])
    }

    boolean isPreviewLogoSet() {
        viewSavedLogo.bytes != null
    }

    private boolean validate() {
        if (!imageFile || !ImageValidator.validate(imageFile.filename)) {
            error = true
        }

        return !error
    }
}
