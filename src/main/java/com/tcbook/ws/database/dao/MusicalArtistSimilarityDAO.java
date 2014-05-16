package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.MusicalArtistSimilarity;

import java.sql.SQLException;

/**
 * Created by caiouvini on 5/15/14.
 */
public interface MusicalArtistSimilarityDAO {

    public void insert(MusicalArtistSimilarity musicalArtistSimilarity) throws SQLException;

}
