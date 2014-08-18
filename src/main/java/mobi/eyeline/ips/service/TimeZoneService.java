package mobi.eyeline.ips.service;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Resources;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Charsets.UTF_8;

public class TimeZoneService {

    private final LoadingCache<Locale, List<Pair<String, String>>> cache =
            CacheBuilder.newBuilder().softValues().build(new TzCacheLoader());

    public List<Pair<String, String>> getZoneNames(Locale locale) {
        try {
            return cache.get(locale);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TzCacheLoader extends CacheLoader<Locale, List<Pair<String, String>>> {
        @Override
        public List<Pair<String, String>> load(Locale key) throws IOException {
            final URL url = getClass().getResource("/tznames_" + key.getLanguage() + ".properties");
            return new ArrayList<Pair<String, String>>() {{
                for (String line : Resources.readLines(url, UTF_8)) {
                    final String[] parts = line.split("=");
                    add(Pair.of(parts[0].trim(), parts[1].trim()));
                }
            }};
        }
    }
}
