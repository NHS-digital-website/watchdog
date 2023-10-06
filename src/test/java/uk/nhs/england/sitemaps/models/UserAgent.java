package uk.nhs.england.sitemaps.models;

import java.util.ArrayList;

public class UserAgent {

    private final String userAgent;
    private final ArrayList<String> disallowed = new ArrayList<>();
    private final ArrayList<String> allowed = new ArrayList<>();
    private final ArrayList<String> sitemaps = new ArrayList<>();

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
