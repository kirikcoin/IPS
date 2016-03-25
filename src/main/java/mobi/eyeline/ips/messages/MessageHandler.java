package mobi.eyeline.ips.messages;

public interface MessageHandler {

  UssdResponseModel handle(String msisdn, AnswerOption request, OuterRequest outerRequest);
  UssdResponseModel handle(String msisdn, UssdOption request, OuterRequest outerRequest);
  UssdResponseModel handle(String msisdn, BadCommandOption request, OuterRequest outerRequest);
  UssdResponseModel handle(String msisdn, ArbitraryAnswerOption request, OuterRequest outerRequest);

}
