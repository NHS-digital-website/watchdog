package uk.nhs.england.sitemaps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class SitemapReducer {

    private static final Logger logger = LogManager.getLogger(SitemapReducer.class);

    /**
     * @param urls The list of URLs to reduce.
     * @return A reduced list of URLs.
     */
    public static List<URL> reduce(final List<URL> urls) {

        logger.debug("Reducing " + urls.size() + " URLs.");
        Set<URL> reducedUrls = new HashSet<>(); // FIXME: 26/10/2023 To ensure that the Set<URL> reducedUrls doesn't contain duplicate URL objects, you can modify it to use a LinkedHashSet. A LinkedHashSet preserves the order of elements while maintaining uniqueness.
        Map<String, URL> uniqueLeafs = new HashMap<>();

        for (URL url : urls) {
            String[] pathParts = url.getPath().split("/");

            if (pathParts.length > 2) {
                String key = url.getAuthority() + String.join("/", Arrays.copyOfRange(pathParts, 0, pathParts.length - 1));
                if (!uniqueLeafs.containsKey(key)) {
                    uniqueLeafs.put(key, url);
                }
            } else {
                reducedUrls.add(url);
            }
        }

        reducedUrls.addAll(new ArrayList<>(uniqueLeafs.values()));

        for (URL url : uniqueLeafs.values()) {
            String[] split = url.toString().split("/");
            URL key = null;
            try {
                key = new URL(String.join("/", Arrays.copyOfRange(split, 0, split.length - 1)));
                if (urls.contains(key)) {
                    reducedUrls.add(key);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        logger.debug("Reduced size " + reducedUrls.size() + " URLs.");
        return new ArrayList<>(reducedUrls);
    }

}
