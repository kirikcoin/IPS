package mobi.eyeline.ips.generators;

public class SequenceGenerator {

    private final Pattern pattern;
    private final NumberBijection numberBijection;
    private long currentPosition;

    SequenceGenerator(Pattern pattern,
                      NumberBijection numberBijection,
                      long currentPosition) {

        this.pattern = pattern;
        this.numberBijection = numberBijection;
        this.currentPosition = currentPosition;
    }

    public SequenceGenerator(Pattern pattern,
                             NumberBijection numberBijection) {
        this(pattern, numberBijection, 0);
    }

    public Pattern getPattern() {
        return pattern;
    }

    /**
     * @return Total number of combinations for this pattern.
     */
    public long getTotal() {
        return pattern.getCapacity();
    }

    /**
     * @return Available number of combinations for this pattern,
     * i.e. taking current position into account.
     */
    public long getAvailable() {
        return getTotal() - getCurrentPosition();
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    /**
     * @return Next pattern, incrementing current position.
     */
    public synchronized CharSequence next() {
        if (currentPosition == getTotal()) {
            return null;
        }

        final long index = numberBijection.apply(currentPosition++);
        return pattern.convert(index);
    }
}
