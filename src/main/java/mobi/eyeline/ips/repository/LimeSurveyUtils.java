package mobi.eyeline.ips.repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 15.04.11
 * Time: 15:25
 */
public class LimeSurveyUtils {
    static public String surveyTable(int sid) {
        return String.format("lime_survey_%s", sid);
    }

    static public String answerField(int sid, int qid, int gid) {
        return String.format("%dX%dX%d", sid, gid, qid);
    }

    public static boolean parseBoolean(String value) {
        return "Y".equalsIgnoreCase(value);
    }

    public static String surveyOldTable(int surveyId, String surveyDate) {
        return String.format("lime_old_survey_%d_%s", surveyId, surveyDate);
    }

    public static String parseOldTableTail(String oldTable) {
        Pattern pattern = Pattern.compile("^lime_old_survey_\\d+_(.*)$");
        Matcher matcher = pattern.matcher(oldTable);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    public static Date parseOldTableDateTime(String s) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return df.parse(s);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
