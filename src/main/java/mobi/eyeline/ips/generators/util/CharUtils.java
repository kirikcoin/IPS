package mobi.eyeline.ips.generators.util;

import com.google.common.collect.Lists;

import java.util.List;

public class CharUtils {

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

    public static CharSequence exclude(CharSequence from, CharSequence what) {
        final StringBuilder buf = new StringBuilder();

        final List<Character> excluded = Lists.charactersOf(what);
        for (Character c : Lists.charactersOf(from)) {
            if (!excluded.contains(c)) {
                buf.append(c);
            }
        }

        return buf.toString();
    }
}
