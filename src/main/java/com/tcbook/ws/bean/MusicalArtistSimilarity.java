package com.tcbook.ws.bean;

/**
 * Created by caiouvini on 5/15/14.
 */
public class MusicalArtistSimilarity {

    private Long musicalArtistId;

    private Long similarMusicalArtistId;

    public Long getMusicalArtistId() {
        return musicalArtistId;
    }

    public void setMusicalArtistId(Long musicalArtistId) {
        this.musicalArtistId = musicalArtistId;
    }

    public Long getSimilarMusicalArtistId() {
        return similarMusicalArtistId;
    }

    public void setSimilarMusicalArtistId(Long similarMusicalArtistId) {
        this.similarMusicalArtistId = similarMusicalArtistId;
    }
}
