package mobi.eyeline.ips.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ListUtils {

    /**
     * Increases index of {@code elem} in the specified list, if possible.
     * All the elements along the way for which the {@code skip} predicate holds true are skipped.
     *
     * @throws java.util.NoSuchElementException In case the list doesn't contain
     *                                          the specified element.
     */
    public static <T> void moveUp(List<T> list, T elem, Predicate<T> skip) {
        int idx = safeIndexOf(list, elem);
        list.remove(idx);

        idx++;
        while ((idx < list.size()) && skip.apply(list.get(idx - 1))) {
            idx++;
        }

        list.add(min(idx, list.size()), elem);
    }

    /**
     * Same as {@link #moveUp(java.util.List, Object, com.google.common.base.Predicate)}
     * without element skipping.
     */
    public static <T> void moveUp(List<T> list, T elem) {
        moveUp(list, elem, Predicates.<T>alwaysFalse());
    }

    /**
     * @see #moveUp(java.util.List, Object, com.google.common.base.Predicate)
     */
    public static <T> void moveDown(List<T> list, T elem, Predicate<T> skip) {
        int idx = safeIndexOf(list, elem);
        list.remove(idx);

        idx--;
        while ((idx >= 0) && skip.apply(list.get(idx))) {
            idx--;
        }

        list.add(max(idx, 0), elem);
    }

    /**
     * Same as {@link #moveDown(java.util.List, Object, com.google.common.base.Predicate)}
     * without element skipping.
     */
    public static <T> void moveDown(List<T> list, T elem) {
        moveDown(list, elem, Predicates.<T>alwaysFalse());
    }

    public static <T> boolean isFirst(List<T> list, T elem, Predicate<T> skip) {
        final int idx = safeIndexOf(list, elem);

        for (int i = 0; i < idx; i++) {
            if (!skip.apply(list.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static <T> boolean isLast(List<T> list, T elem, Predicate<T> skip) {
        final int idx = safeIndexOf(list, elem);

        for (int i = list.size() - 1; i > idx; i--) {
            if (!skip.apply(list.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static <T> T getNext(List<T> list, T elem, Predicate<T> skip) {
        int idx = safeIndexOf(list, elem);

        do {
            idx++;
        } while ((idx < list.size()) && skip.apply(list.get(idx)));

        return (idx >= list.size()) ? null : list.get(idx);
    }

    private static <T> int safeIndexOf(List<T> list, T elem) {
        final int idx = list.indexOf(elem);
        if (idx < 0) {
            throw new NoSuchElementException();
        }
        return idx;
    }
}
