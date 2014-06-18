package mobi.eyeline.ips.generators;

public interface Pattern {
    public CharSequence convert(long number);
    public long convert(CharSequence value);
    public long getCapacity();
    public String getPattern();
}
