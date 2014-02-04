package mobi.eyeline.ips.web.controllers.clients

import mobi.eyeline.ips.model.Role
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController

import javax.faces.model.SelectItem

class ClientController extends BaseController {

    private final UserRepository userRepository = Services.instance().userRepository

    List<SelectItem> getClients() {
        return userRepository
                .listByRole(Role.CLIENT)
                .findAll { !it.blocked }
                .collect { new SelectItem(it.id, it.fullName) }
    }

}
