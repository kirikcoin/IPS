package mobi.eyeline.ips.messages;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class UssdModel {

    private final String text;
    private final List<UssdOption> options;

    public UssdModel(String text, List<? extends UssdOption> options) {
        this.text = requireNonNull(text);
        this.options = Collections.unmodifiableList(options);
    }

    public UssdModel(String text) {
        this(text, Collections.<UssdOption>emptyList());
    }

    public String getText() {
        return text;
    }

    public List<UssdOption> getOptions() {
        return options;
    }
}
