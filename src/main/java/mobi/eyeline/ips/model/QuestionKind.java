package mobi.eyeline.ips.model;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 15.04.11
 * Time: 13:20
  */
public enum QuestionKind {
    Unknown(null),
    LongFreeText('T'),
    ListRadio('L');

    private final Character shortName;

    private QuestionKind(Character shortName) {
        this.shortName = shortName;
    }

    public char getShortName() {
        return shortName;
    }

    public static QuestionKind fromShortName(Character shortName) {
        switch (shortName) {
            case 'T':   return LongFreeText;
            case 'L':   return ListRadio;
            default:    return Unknown;
        }
    }
}
