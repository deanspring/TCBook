package com.tcbook.ws.database.dao;

import java.sql.SQLException;
import java.util.Map;

public interface PersonLikeMusicalArtistDAO {

	public void insert(Long idPerson, Long idMusicalArtist, Integer rating) throws SQLException;

	public void remove(Long idPerson, Long idMusicalArtist) throws SQLException;

	public void removeAllForArtist(Long idMusicalArtist) throws SQLException;

	public void removeAllForPerson(Long idPerson) throws SQLException;

	public Map<Long, Integer> getAllForPerson(Long idPerson) throws SQLException;

	public void updateLike(Long idPerson, Long idMusicalArtist, Integer rate) throws SQLException;

	public void dataCleaning();

}
