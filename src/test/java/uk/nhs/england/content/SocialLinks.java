package uk.nhs.england.content;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import uk.nhs.england.utils.helpers.SystemProperties;

import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SocialLinks {

    private static final Logger logger = LogManager.getLogger(SocialLinks.class);
    private final String address;
    private final Function<Document, Elements> linkExtractor;
    private List<String> cachedLinks = null;

    public SocialLinks(String address, Function<Document, Elements> linkExtractor) {
        this.address = address;
        this.linkExtractor = linkExtractor;
    }

    public List<String> getLinks() {
        if (cachedLinks == null) {
            this.cachedLinks = new ArrayList<>();;
            try {
                Document document = Jsoup.connect(address).userAgent(SystemProperties.getUserAgent()).get();
                Elements links = linkExtractor.apply(document);
                cachedLinks = links.stream()
                        .map(link -> link.attr("href"))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                logger.error("Failed to fetch links from {}", address, e);
                return new ArrayList<>();
            }
        }
        return new ArrayList<>(cachedLinks);
    }

}
