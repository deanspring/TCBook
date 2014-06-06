package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.bean.MusicalGenre;

import java.sql.SQLException;
import java.util.List;

public interface MusicalArtistDAO {

	public MusicalArtist find(Long id);

	public List<MusicalArtist> findAll();

	public String findAllAsJson();

	public void insert(MusicalArtist artist) throws SQLException;

	public void remove(Long id) throws SQLException;

	public void update(MusicalArtist artist) throws SQLException;

	public List<MusicalGenre> getGenresOfArtist(Long artistId);

	public String toJson(List<MusicalArtist> artists);

}
