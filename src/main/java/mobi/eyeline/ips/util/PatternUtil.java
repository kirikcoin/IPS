package mobi.eyeline.ips.util;

import mobi.eyeline.ips.model.SurveyPattern;

public class PatternUtil {

  public static String asRegex(SurveyPattern.Mode mode, int length) {
    assert length > 1;

    final StringBuilder builder = new StringBuilder();

    // Prohibit first symbol to be `0'.
    if (mode == SurveyPattern.Mode.DIGITS) {
      builder.append("[1-9]").append("[0-9]");

    } else if (mode == SurveyPattern.Mode.DIGITS_AND_LATIN) {
      builder.append("[1-9A-Z]").append("[0-9A-Z]");

    } else {
      throw new AssertionError("Unknown pattern mode: " + mode);
    }

    builder.append("{").append(length - 1).append("}");
    return builder.toString();
  }
}
