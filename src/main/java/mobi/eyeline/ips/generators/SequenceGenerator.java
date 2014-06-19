package mobi.eyeline.ips.generators;

/**
 * @author andy
 */
public interface SequenceGenerator {
    Pattern getPattern();

    /**
     * @return Total number of combinations for this pattern.
     */
    long getTotal();

    /**
     * @return Available number of combinations for this pattern,
     * i.e. taking current position into account.
     */
    long getRemaining();

    long getCurrentPosition();

    /**
     * @return Next pattern, incrementing current position.
     */
    CharSequence next();
}
