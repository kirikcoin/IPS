package mobi.eyeline.ips.messages;

import static mobi.eyeline.ips.messages.RegistrationOption.RegistrationAccepted;
import static mobi.eyeline.ips.messages.RegistrationOption.RegistrationDeclined;

public interface MessageHandler {

    public UssdModel handle(String msisdn, RegistrationAccepted request);
    public UssdModel handle(String msisdn, RegistrationDeclined request);
    public UssdModel handle(String msisdn, AnswerOption request);
    public UssdModel handle(String msisdn, UssdOption request);

}
