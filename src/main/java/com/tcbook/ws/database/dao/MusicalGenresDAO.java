package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.MusicalGenre;

import java.sql.SQLException;
import java.util.List;

public interface MusicalGenresDAO {

	public List<MusicalGenre> findAll() throws SQLException;

	public void insert(String genreName) throws SQLException;

	public MusicalGenre getForName(String genreName) throws SQLException;

}
