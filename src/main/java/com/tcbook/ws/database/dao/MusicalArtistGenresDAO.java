package com.tcbook.ws.database.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MusicalArtistGenresDAO {

	public void insert(Long idMusicalArtist, Long idGenre) throws SQLException;

	public void removeAllForArtist(Long idMusicalArtist) throws SQLException;

    public Map<Long, Integer> topFivePopularGenres();

    public Map<Long, Integer> getGroupedGenresForArtists(List<Long> artists);

    public List<Long> findGenresByArtist(Long artistId);

}
