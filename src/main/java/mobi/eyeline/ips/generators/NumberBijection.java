package mobi.eyeline.ips.generators;

/**
 * Represents a function such as {@literal f: P -> P}, where:
 * <ol>
 *     <li>{@literal P} is a pre-defined numeric range {@code [0..MAX_VALUE)}</li>
 *     <li>{@literal f} basically performs a permutation, i.e. it is a guaranteed that
 *     if {@code x} traverses from {@code 0} to {@code MAX_VALUE-1}, {@code f(x)} will traverse
 *     all values of {@literal P} producing each element only once.
 *     </li>
 * </ol>
 *
 * <br/>
 * For instance, {@code f(x) = x } is such a function.
 */
public interface NumberBijection {
    long apply(long from);
}
