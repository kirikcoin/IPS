package mobi.eyeline.ips.web.controllers.clients

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.Locale as IpsLocale
import mobi.eyeline.ips.model.Role
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.web.controllers.BaseController

import javax.enterprise.inject.Model
import javax.faces.model.SelectItem
import javax.inject.Inject

@CompileStatic
@Slf4j('logger')
@Model
class ClientController extends BaseController {

  @Inject private UserRepository userRepository

  /**
   * List of clients to associate w/ survey.
   */
  List<SelectItem> getClients(User currentManager) {
    if (currentManager.role != Role.MANAGER) {
      logger.error "Non-manager account requested client list, user = [$currentManager]"
      throw new AssertionError()
    }

    return userRepository
        .listClients(currentManager.showAllClients ? null : currentManager)
        .findAll { !it.blocked }
        .collect { new SelectItem(it.id, it.fullName) }
  }

  @SuppressWarnings("GrMethodMayBeStatic")
  List<SelectItem> getLocales() {
    def localeName = { IpsLocale ipsLocale ->
      ResourceBundle
          .getBundle('ips', ipsLocale.asLocale())
          .getString('locale.name.select')
    }

    return IpsLocale.values().collect { IpsLocale it -> new SelectItem(it, localeName(it)) }
  }
}
