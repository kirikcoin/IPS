package mobi.eyeline.ips.generators.impl;

import mobi.eyeline.ips.generators.Pattern;
import mobi.eyeline.ips.generators.util.CharUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplePattern implements Pattern {

  private final CharSequence[] options;
  private final long capacity;

  public SimplePattern(CharSequence[] options) {
    this.options = unify(options);
    this.capacity = CharUtils.permutations(this.options);
  }

  private CharSequence[] unify(CharSequence[] options) {
    for (int i = 0; i < options.length; i++) {
      options[i] = CharUtils.sort(options[i]);
    }
    return options;
  }

  @Override
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

  @Override
  public long convert(CharSequence value) {
    throw new AssertionError();
  }

  @Override
  public final long getCapacity() {
    return capacity;
  }

  public SimplePattern minus(SimplePattern pattern) {
    if (this.options.length != pattern.options.length) {
      return this;
    }

    final List<CharSequence> results = new ArrayList<>();
    for (int i = 0; i < options.length; i++) {
      final CharSequence result = CharUtils.exclude(options[i], pattern.options[i]);
      if (result.length() == 0) {
        return new SimplePattern(new CharSequence[0]);
      }
      results.add(result);
    }

    return new SimplePattern(results.toArray(new CharSequence[results.size()]));
  }

  public String getPattern() {
    final StringBuilder builder = new StringBuilder();
    for (CharSequence option : options) {
      builder.append("[").append(option).append("]");
    }
    return builder.toString();
  }

  @Override
  public String toString() {
    return getPattern();
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
