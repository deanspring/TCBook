package com.tcbook.ws.database.dao;

import java.sql.SQLException;

public interface MusicalArtistGenresDAO {

	public void insert(Long idMusicalArtist, Long idGenre) throws SQLException;

	public void removeAllForArtist(Long idMusicalArtist) throws SQLException;

}
