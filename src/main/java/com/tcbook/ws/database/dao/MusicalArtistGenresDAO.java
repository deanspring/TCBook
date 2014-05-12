package com.tcbook.ws.database.dao;

import java.sql.SQLException;

/**
 * Created by caiouvini on 5/11/14.
 */
public interface MusicalArtistGenresDAO {

    public void insert(Long idMusicalArtist, Long idGenre) throws SQLException;

    public void removeAllForArtist(Long idMusicalArtist) throws SQLException;

}
