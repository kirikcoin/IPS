package mobi.eyeline.ips.web.controllers.settings

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.InvitationDelivery
import mobi.eyeline.ips.model.UiProfile
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.web.controllers.BaseController

import javax.faces.model.SelectItem

@CompileStatic
class SettingsPageController extends BaseController {

    User user

    final List<SelectItem> skins = UiProfile.Skin.values().collect {
        UiProfile.Skin skin -> new SelectItem(skin.toString(), nameOf(skin))
    }

    SettingsPageController() {
        this.user = getCurrentUser()
        user.uiProfile = new UiProfile(id:1, icon: new byte[10], skin: UiProfile.Skin.ARAKS)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    static String nameOf(UiProfile.Skin skin) {
        //noinspection UnnecessaryQualifiedReference
        BaseController.strings["settings.skin.$skin".toString()]
    }

}
