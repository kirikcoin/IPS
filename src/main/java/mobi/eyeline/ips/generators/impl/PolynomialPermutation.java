package mobi.eyeline.ips.generators.impl;

import com.google.common.collect.Multiset;
import mobi.eyeline.ips.generators.NumberBijection;
import mobi.eyeline.ips.generators.util.PrimeUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Simple implementation of finite range permutation using quadratic permutation polynomials.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Permutation_polynomials">Wikipedia on QPP</a>
 */
public class PolynomialPermutation implements NumberBijection {

  private final long maxValue;
  private final Expression expression;

  public PolynomialPermutation(long maxValue) {
    if (maxValue <= 0) {
      throw new IllegalArgumentException();
    }

    this.maxValue = maxValue;
    this.expression = createExpression();
  }

  private Expression createExpression() {
    // Let maxValue = p1^k1 * p2^k2 * .. * pn^kn.
    final Multiset<Long> primes = PrimeUtils.factorize(maxValue);

    final long p1 = getMultiPrime(primes);

    // a = p1 * p2^k2 * .. * pn^kn.
    long a = p1;
    for (Long prime : primes.elementSet()) {
      if (prime != p1) {
        a *= BigInteger.valueOf(prime).pow(primes.count(prime)).longValue();
      }
    }

    // a * x^2 + x + (maxValue / 3 - 1)
    // The latter coefficient is an arbitrary constant - the value doesn't really matter
    // as all the computations are performed modulo maxValue.
    return new Expression(a, 1, maxValue / 3 - 1);
  }

  /**
   * Determines the maximal of prime factors {@literal P} of {@linkplain #maxValue set size}
   * for which the set size is divisible by {@code P^2}.
   * <br/>
   * The maximal one is taken just to extend the variation whereas
   * it's not a mandatory requirement for QPP.
   */
  private long getMultiPrime(Multiset<Long> primes) {
    final List<Long> multiprimes = new ArrayList<>();
    for (Long prime : primes.elementSet()) {
      if (primes.count(prime) > 1) {
        multiprimes.add(prime);
      }
    }

    try {
      return Collections.max(multiprimes);
    } catch (NoSuchElementException e) {
      // Well, if there's no prime P (!= 1) for which the number N of elements in our set
      // can be divided by P^2, QPP construction is impossible.
      throw new IllegalArgumentException(
          "Invalid range for polynomial permutation: " + maxValue);
    }
  }

  @Override
  public long apply(long from) {
    assert from >= 0 && from < maxValue;

    return expression.apply(from).mod(BigInteger.valueOf(maxValue)).longValue();
  }

  @Override
  public String toString() {
    return String.format("%s (mod %d)", expression, maxValue);
  }

  /**
   * {@code a * x^2 + b * x + c} polynomial.
   */
  private static class Expression {
    final BigInteger a;
    final BigInteger b;
    final BigInteger c;

    public Expression(long a, long b, long c) {
      this.a = BigInteger.valueOf(a);
      this.b = BigInteger.valueOf(b);
      this.c = BigInteger.valueOf(c);
    }

    public BigInteger apply(long x) {
      return apply(BigInteger.valueOf(x));
    }

    private BigInteger apply(BigInteger x) {
      return x.pow(2).multiply(a).add(b.multiply(x)).add(c);
    }

    @Override
    public String toString() {
      return String.format("%d * x^2 + %d * x + %d", a, b, c);
    }
  }
}
