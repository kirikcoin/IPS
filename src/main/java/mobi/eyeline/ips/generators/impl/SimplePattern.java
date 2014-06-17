package mobi.eyeline.ips.generators.impl;

import mobi.eyeline.ips.generators.util.CharUtils;
import mobi.eyeline.ips.generators.Pattern;

import java.util.Arrays;

public class SimplePattern implements Pattern {

    private final CharSequence[] options;
    private final long capacity;

    public SimplePattern(CharSequence[] options) {
        this.options = options;
        this.capacity = CharUtils.permutations(options);
    }

    public CharSequence convert(long number) {
        assert (number >= 0) && (number < capacity) :
                "Argument outside of input range: " + number;

        final StringBuilder builder = new StringBuilder(options.length);

        for (int nChar = options.length - 1; nChar >= 0; nChar--) {
            final CharSequence symbols = options[nChar];
            final int count = symbols.length();

            final int symbolNum = (int) (number % count);
            builder.append(symbols.charAt(symbolNum));

            number = (number - symbolNum) / count;
        }

        return builder.reverse().toString();
    }

    public long convert(CharSequence value) {
        throw new AssertionError();
    }

    public final long getCapacity() {
        return capacity;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimplePattern)) return false;
        return Arrays.equals(options, ((SimplePattern) o).options);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(options);
    }
}
