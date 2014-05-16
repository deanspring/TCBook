package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.SimilarMusicalArtist;

import java.sql.SQLException;

/**
 * Created by caiouvini on 5/15/14.
 */
public interface SimilarMusicalArtistDAO {

    public void insert(SimilarMusicalArtist similarMusicalArtist) throws SQLException;

    public SimilarMusicalArtist getByName(String name);

    public SimilarMusicalArtist getByMbid(String mbid);

}
