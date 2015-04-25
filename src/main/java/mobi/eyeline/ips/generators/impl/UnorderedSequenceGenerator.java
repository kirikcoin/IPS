package mobi.eyeline.ips.generators.impl;

import mobi.eyeline.ips.generators.NumberBijection;
import mobi.eyeline.ips.generators.Pattern;
import mobi.eyeline.ips.generators.SequenceGenerator;

public class UnorderedSequenceGenerator implements SequenceGenerator {

  private final Pattern pattern;
  private final NumberBijection numberBijection;
  private long currentPosition;

  public UnorderedSequenceGenerator(Pattern pattern,
                                    NumberBijection numberBijection,
                                    long currentPosition) {

    this.pattern = pattern;
    this.numberBijection = numberBijection;
    this.currentPosition = currentPosition;
  }

  public UnorderedSequenceGenerator(Pattern pattern,
                                    NumberBijection numberBijection) {
    this(pattern, numberBijection, 0);
  }

  @Override
  public Pattern getPattern() {
    return pattern;
  }

  @Override
  public long getTotal() {
    return pattern.getCapacity();
  }

  @Override
  public long getRemaining() {
    return getTotal() - getCurrentPosition();
  }

  @Override
  public long getCurrentPosition() {
    return currentPosition;
  }

  @Override
  public synchronized CharSequence next() {
    if (currentPosition == getTotal()) {
      return null;
    }

    final long index = numberBijection.apply(currentPosition++);
    return pattern.convert(index);
  }

  @Override
  public double getPercentAvailable() {
    if (getRemaining() == 0) {
      return 0;
    }
    return (((double) getRemaining()) / getTotal()) * 100;
  }

  @Override
  public String toString() {
    return "SequenceGenerator{" +
        "pattern=" + pattern +
        ", currentPosition=" + currentPosition +
        ", remaining=" + getRemaining() +
        '}';
  }
}
