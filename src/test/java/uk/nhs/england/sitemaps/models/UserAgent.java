package uk.nhs.england.sitemaps.models;

import java.util.ArrayList;

/**
 * Represents a single user-agent block from a robots.txt file.
 */
public class UserAgent {

    private final String userAgent;
    private ArrayList<String> disallowed = new ArrayList<>();
    private ArrayList<String> allowed = new ArrayList<>();
    private ArrayList<String> sitemaps = new ArrayList<>();
    public UserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public ArrayList<String> getDisallowed() {
        return disallowed;
    }

    public ArrayList<String> getAllowed() {
        return allowed;
    }

    public ArrayList<String> getSitemaps() {
        return sitemaps;
    }

}
