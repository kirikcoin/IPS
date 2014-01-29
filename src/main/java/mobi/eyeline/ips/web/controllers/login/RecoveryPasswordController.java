package mobi.eyeline.ips.web.controllers.login;


import mobi.eyeline.ips.exceptions.LoginException;
import mobi.eyeline.ips.service.Services;
import mobi.eyeline.ips.service.UserService;

import java.io.Serializable;

public class RecoveryPasswordController implements Serializable {
    private UserService userService;
    private String email;
    public RecoveryPasswordController(){
        userService = Services.instance().getUserService();
    }

    public String recovery() {
        try {
            userService.restorePassword(email);
            return "donerecovery";
        } catch (LoginException e) {
            return "retryrecovery";
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
