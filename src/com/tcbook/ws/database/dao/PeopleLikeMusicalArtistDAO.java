package com.tcbook.ws.database.dao;

import java.sql.SQLException;

public interface PeopleLikeMusicalArtistDAO {

    public void insert(Long idPeople, Long idMusicalArtist, Integer rating) throws SQLException;

    public void remove(Long idPeople, Long idMusicalArtist) throws SQLException;

    public void removeAllForArtist(Long idMusicalArtist) throws SQLException;

    public void removeAllForPeople(Long idPeople) throws SQLException;

}
