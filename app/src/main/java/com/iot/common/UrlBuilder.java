package com.iot.common;

/**
 * Created by cristianmiranda on 6/4/16.
 */
public class UrlBuilder {
    private static final String BASE_URL = "http://192.168.1.72:8080/app/rest/";

    public static String build(String path) {
        return BASE_URL + path;
    }
}
