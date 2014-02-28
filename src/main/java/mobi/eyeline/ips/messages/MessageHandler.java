package mobi.eyeline.ips.messages;

public interface MessageHandler {

    public UssdResponseModel handle(String msisdn, AnswerOption request);
    public UssdResponseModel handle(String msisdn, UssdOption request);

}
