package com.tcbook.ws.database.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PersonLikeMusicalArtistDAO {

	public void insert(Long idPerson, Long idMusicalArtist, Integer rating) throws SQLException;

	public void remove(Long idPerson, Long idMusicalArtist) throws SQLException;

	public void removeAllForArtist(Long idMusicalArtist) throws SQLException;

	public void removeAllForPerson(Long idPerson) throws SQLException;

	public Map<Long, Integer> getAllForPerson(Long idPerson) throws SQLException;

	public void updateLike(Long idPerson, Long idMusicalArtist, Integer rate) throws SQLException;

	public void dataCleaning();

    public Map<String, Float> generalRatings();

    public Map<Long, Float> averageByArtist();

    public Map<Long, Float> topTwentyAveragesByArtist();

    public Map<Long, Integer> topTenPopularArtists();

    public Map<Long, Float> topTenStandardDeviationByArtist();

    public Map<Long, Integer> artistsPopularity();

    public Map<Integer, Integer> peopleLikesByAmount();

    public Map<Integer, Integer> artistsLikesByAmount();

    public List<Long> artistsForPersonWithMinimumRate(Long personId, Integer minimumRate);

    public Map<Long, Double> averagesForArtists(List<Long> artists);

    public Map<Long, Integer> artistsLikesByFriends(List<Long> artists, Long personId);
}
