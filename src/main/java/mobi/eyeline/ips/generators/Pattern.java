package mobi.eyeline.ips.generators;

/**
 * Bijection from a finite range {@code [0..N-1]} to a predefined set of words {@code L}.
 */
public interface Pattern {

    CharSequence convert(long number);

    long convert(CharSequence value);

    /**
     * @return Range upper threshold.
     */
    long getCapacity();
}
