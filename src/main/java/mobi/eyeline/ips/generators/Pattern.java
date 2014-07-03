package mobi.eyeline.ips.generators;

/**
 * Bijection from a finite range {@code [0..N-1]} to a predefined set of words {@code L}.
 */
public interface Pattern {

    /**
     * Bijection {@code [0..N-1] -> L}.
     */
    CharSequence convert(long number);

    /**
     * Bijection {@code L -> [0..N-1]}.
     */
    long convert(CharSequence value);

    /**
     * @return Range upper threshold aka {@code N}.
     */
    long getCapacity();
}
