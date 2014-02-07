package mobi.eyeline.ips.messages;

public class MissingParameterException extends Exception {

    public MissingParameterException(String parameterName) {
        super("Missing required parameter [" + parameterName + "]");
    }
}
