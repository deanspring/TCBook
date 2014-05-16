package com.tcbook.ws.bean;

/**
 * Created by caiouvini on 5/15/14.
 */
public class SimilarMusicalArtist {

    private Long id;

    private String artisticName;

    private String mbid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtisticName() {
        return artisticName;
    }

    public void setArtisticName(String artisticName) {
        this.artisticName = artisticName;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }
}
