package mobi.eyeline.ips.messages;

import java.util.Collections;
import java.util.List;

public class UssdModel {

    private final String text;
    private final List<UssdOption> options;

    public UssdModel(String text, List<? extends UssdOption> options) {
        assert text != null;

        this.text = text;
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
