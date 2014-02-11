package mobi.eyeline.ips.messages;

public interface MessageHandler {

    public UssdModel handle(String msisdn, AnswerOption request);
    public UssdModel handle(String msisdn, UssdOption request);

}
