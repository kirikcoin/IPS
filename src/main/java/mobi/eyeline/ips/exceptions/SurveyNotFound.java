package mobi.eyeline.ips.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 22.04.11
 * Time: 18:20
 */
public class SurveyNotFound extends Exception {
    public int sid;

    public SurveyNotFound(int sid) {
        super();
        this.sid = sid;
    }
}
