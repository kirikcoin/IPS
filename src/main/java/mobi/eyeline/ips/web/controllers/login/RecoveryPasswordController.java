package mobi.eyeline.ips.web.controllers.login;


import mobi.eyeline.ips.exceptions.LoginException;
import mobi.eyeline.ips.service.Services;
import mobi.eyeline.ips.service.UserService;
import mobi.eyeline.ips.web.controllers.BaseController;

public class RecoveryPasswordController extends BaseController {
    private UserService userService;
    private String email;
    private boolean retry;
    public RecoveryPasswordController(){
        userService = Services.instance().getUserService();
    }

    public String recovery() {
        try {
            userService.restorePassword(email);
            return "DONE_RECOVERY";
        } catch (LoginException e) {
            return "RETRY_RECOVERY";
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }
}
