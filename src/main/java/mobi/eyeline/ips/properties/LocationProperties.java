package mobi.eyeline.ips.properties;

import java.util.regex.Pattern;

public class LocationProperties {
    private final String pattern;
    private final String skin;

    private final Pattern compiledPattern;

    public LocationProperties(String pattern, String skin) {
        this.pattern = pattern;
        this.skin = skin;

        compiledPattern = Pattern.compile(pattern);
    }

    public String getPattern() {
        return pattern;
    }

    public String getSkin() {
        return skin;
    }

    public Pattern getCompiledPattern() {
        return compiledPattern;
    }
}
