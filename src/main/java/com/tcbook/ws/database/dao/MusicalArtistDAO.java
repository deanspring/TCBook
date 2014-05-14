package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.MusicalArtist;

import java.sql.SQLException;
import java.util.List;

public interface MusicalArtistDAO {

	public MusicalArtist find(Long id);

	public List<MusicalArtist> findAll();

	public void insert(MusicalArtist artist) throws SQLException;

	public void remove(Long id) throws SQLException;

	public void update(MusicalArtist artist) throws SQLException;

}
