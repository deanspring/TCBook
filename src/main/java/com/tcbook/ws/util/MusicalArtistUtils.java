package com.tcbook.ws.util;

import com.tcbook.ws.bean.MusicalArtist;

import java.net.URLDecoder;

/**
 * Created by caiouvini on 5/20/14.
 */
public class MusicalArtistUtils {

    public static String getArtistNameFromWikipediaURLNormalized(MusicalArtist artist) throws Exception {
        return normalize(artist.getUrl().replace("http://en.wikipedia.org/wiki/", ""));
    }

    private static String normalize(String s) throws Exception {
        return URLDecoder.decode(s.toLowerCase(), "UTF-8").replaceAll("_\\(.+\\)", "").replaceAll("_", " ");
    }

}
