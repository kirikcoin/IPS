package mobi.eyeline.ips.generators.util;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class PrimeUtils {

    public static Multiset<Long> factorize(long n) {
        final Multiset<Long> factors = HashMultiset.create();

        for (long i = 2; i <= n / i; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }

        if (n > 1) {
            factors.add(n);
        }

        return factors;
    }
}
