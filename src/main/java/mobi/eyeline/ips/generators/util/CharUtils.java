package mobi.eyeline.ips.generators.util;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.charactersOf;
import static com.google.common.collect.Lists.newArrayList;

public class CharUtils {

    /**
     * Returns a number of valid strings passing an expression of the form
     * <pre>
     *     [a_11..a_1x][a_21..a_2y]..[a_n1..a_nz]
     * </pre>
     */
    public static long permutations(CharSequence[] sequence) {
        if (sequence.length == 0) {
            return 0;
        }

        long total = 1;
        for (CharSequence option : sequence) {
            total *= option.length();
        }
        return total;
    }

    public static void collect(StringBuilder target,
                               char from,
                               char to) {
        assert to >= from;

        for (char c = from; c <= to; c++) {
            target.append(c);
        }
    }

    /**
     * @return A sequence consisting of chars present in {@code from} and missing in {@code what}.
     * The order of the resulting sequence is defined by element index in {@code from}.
     */
    public static CharSequence exclude(CharSequence from, CharSequence what) {
        final StringBuilder buf = new StringBuilder();

        final List<Character> excluded = charactersOf(what);
        for (Character c : charactersOf(from)) {
            if (!excluded.contains(c)) {
                buf.append(c);
            }
        }

        return buf.toString();
    }

    public static CharSequence sort(CharSequence source) {
        final List<Character> characters = newArrayList(charactersOf(source));
        Collections.sort(characters);

        final StringBuilder buf = new StringBuilder(source.length());
        for (Character c : characters) {
            buf.append(c);
        }
        return buf.toString();
    }
}
