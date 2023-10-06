package uk.nhs.england.sitemaps.models;

import java.net.URL;

public class Result {

    private final URL url;
    private final int responseCode;

    public Result(final URL url, int responseCode) {
        this.url = url;
        this.responseCode = responseCode;
    }

    public URL getUrl() {
        return url;
    }

    public int getResponseCode() {
        return responseCode;
    }

}
