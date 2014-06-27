package mobi.eyeline.ips.generators.impl;

import mobi.eyeline.ips.generators.Pattern;
import mobi.eyeline.ips.generators.SequenceGenerator;

public class EmptyGenerator implements SequenceGenerator {

    @Override
    public Pattern getPattern() {
        return new SimplePattern(new CharSequence[0]);
    }

    @Override
    public long getTotal() {
        return 0;
    }

    @Override
    public long getRemaining() {
        return 0;
    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public CharSequence next() {
        throw new AssertionError();
    }

    @Override
    public double getPercentAvailable() {
        return 0;
    }
}
