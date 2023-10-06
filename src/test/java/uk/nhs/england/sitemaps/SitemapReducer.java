package uk.nhs.england.sitemaps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Tool(s) to reduce a list of URLs that were presumably taken from a sitemap.
 */
public class SitemapReducer {

    private static final Logger logger = LogManager.getLogger(SitemapReducer.class);

    /**
     * Reduces a list of URLs to one representative leaf URL from each branch of a URL hierarchy.
     *
     * For example, given the following list of URLs:
     *  https://example.com/news/company/item-1
     *  https://example.com/news/company/item-2
     *  https://example.com/news/events/item-1
     *  https://example.com/news/events/item-2
     * It would be reduced to:
     *  https://example.com/news/company/item-1
     *  https://example.com/news/events/item-1
     *
     * If a directory (a branch) doubles as a leaf, it is included in the reduced list.
     *
     * For example, given the following list of URLs:
     *  https://example.com/news/company/item-1
     *  https://example.com/news/company/item-2
     *  https://example.com/news/company
     * It would be reduced to:
     *  https://example.com/news/company/item-1
     *  https://example.com/news/company
     *
     * @param urls The list of URLs to reduce.
     * @return A reduced list of URLs.
     */
    public static List<URL> reduce(final List<URL> urls) {

        logger.debug("Reducing " + urls.size() + " URLs.");

        // A set to store the final reduced list.
        Set<URL> reducedUrls = new HashSet<>();

        // Iterate through the list and find one leaf example for each branch.
        Map<String, URL> uniqueLeafs = new HashMap<>();
        for (URL url : urls) {
            String[] parts = url.getPath().split("/");

            if(parts.length > 2) {
                String key = url.getAuthority() + String.join("/", Arrays.copyOfRange(parts, 0, parts.length - 1));
                if(!uniqueLeafs.containsKey(key)) {
                    uniqueLeafs.put(key, url);
                }
            } else {
                // While here, add root level leaf.
                reducedUrls.add(url);
            }
        }

        // Add the kept leafs to the reduced list.
        reducedUrls.addAll(uniqueLeafs.values().stream().collect(Collectors.toList()));


        // Using the keys in uniqueLeafs list to find all of the branches that double as a leaf.
        //
        // For example, given the follow list of URLs:
        //  https://example.com/news/company
        //  https://example.com/news/company/item-1
        //  https://example.com/news/company/item-2
        // The URL https://example.com/news/company is also a leaf (i.e. a page in its own right).
        //
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

        // Return the reduced list.
        return reducedUrls.stream().collect(Collectors.toList());
    }

}
