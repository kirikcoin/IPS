package mobi.eyeline.ips.web.auth;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.util.Map;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
public class IPSLoginModule implements LoginModule {
    private Subject subject;
    private CallbackHandler callbackHandler;

    private String password;
    private String name;


    @Override
    public boolean abort() throws LoginException {
        return true;
    }

    @Override
    public boolean commit() throws LoginException {
        IPSAuthenticator auth = new IPSAuthenticator();
        String roleName = auth.getUserRole(name, password);
        if (roleName == null)
            return false;

        subject.getPrincipals().add(new WebUser(name));
        subject.getPrincipals().add(new WebRole(roleName));
        return true;
    }

    @Override
    public void initialize(Subject subject,
                           CallbackHandler callbackHandler,
                           Map<String, ?> sharedState,
                           Map<String, ?> options) {

        this.subject = subject;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public boolean login() throws LoginException {
        NameCallback name = new NameCallback("User name");
        PasswordCallback password = new PasswordCallback("Password", true);
        try {
            this.callbackHandler.handle(new Callback[] {name, password});
            this.name = name.getName();
            this.password = new String(password.getPassword());

        } catch (Exception e) {
            throw new LoginException(e.getMessage());
        }

        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        return true;
    }
}
