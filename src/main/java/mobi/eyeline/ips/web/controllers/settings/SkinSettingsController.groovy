package mobi.eyeline.ips.web.controllers.settings

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.UiProfile
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.controllers.LogoBean
import mobi.eyeline.ips.web.validators.ImageValidator
import mobi.eyeline.util.jsf.components.input_file.UploadedFile

import javax.annotation.PostConstruct
import javax.enterprise.inject.Model
import javax.inject.Inject

@SuppressWarnings('UnnecessaryQualifiedReference')
@CompileStatic
@Model
class SkinSettingsController extends BaseController {

  @Inject private UserRepository userRepository

  User user
  UploadedFile imageFile
  Boolean error

  /** Stored in session */
  @Inject LogoBean previewLogo

  LogoBean viewSavedLogo

  List<UiProfile.Skin> getSkins() { UiProfile.Skin.values() as List }

  @PostConstruct
  void initBeans() {
    user = getCurrentUser()

    if (!viewSavedLogo?.bytes) {
      viewSavedLogo = new LogoBean(bytes: user.uiProfile.icon)
    }

    previewLogo.bytes = viewSavedLogo.bytes
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
    if (!imageFile) {
      error = true
      addErrorMessage(strings['settings.validation.logo.missing'], 'logo')

    } else if (imageFile.length < 0) {
      error = true
      addErrorMessage(strings['settings.validation.logo.heavy'], 'logo')

    } else if (!new ImageValidator().validate(imageFile.filename)) {
      error = true
      addErrorMessage(strings['settings.validation.logo'], 'logo')

    } else {
      viewSavedLogo = new LogoBean(bytes: imageFile.inputStream.bytes)
      previewLogo.bytes = viewSavedLogo.bytes
    }
  }

  void deleteLogo() {
    viewSavedLogo = new LogoBean(bytes: null as byte[])
  }

  boolean isPreviewLogoSet() {
    viewSavedLogo.bytes != null
  }
}
