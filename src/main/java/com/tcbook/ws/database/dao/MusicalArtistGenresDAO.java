package com.tcbook.ws.database.dao;

import java.sql.SQLException;
import java.util.Map;

public interface MusicalArtistGenresDAO {

	public void insert(Long idMusicalArtist, Long idGenre) throws SQLException;

	public void removeAllForArtist(Long idMusicalArtist) throws SQLException;

    public Map<Long, Integer> topFivePopularGenres();

}
