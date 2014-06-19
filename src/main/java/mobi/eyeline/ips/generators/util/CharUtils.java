package mobi.eyeline.ips.generators.util;

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
}
