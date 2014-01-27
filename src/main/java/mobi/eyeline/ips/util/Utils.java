package mobi.eyeline.ips.util;

import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Utils {

    public static <T> void moveUp(List<T> list, T elem) {
        final int idx = list.indexOf(elem);
        if (idx < 0) {
            throw new NoSuchElementException();
        }

        list.remove(idx);
        list.add(min(idx + 1, list.size()), elem);
    }

    public static <T> void moveDown(List<T> list, T elem) {
        final int idx = list.indexOf(elem);
        if (idx < 0) {
            throw new NoSuchElementException();
        }

        list.remove(idx);
        list.add(max(idx - 1, 0), elem);
    }

}
