package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.MusicalGenre;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by caiouvini on 5/11/14.
 */
public interface MusicalGenresDAO {

    public List<MusicalGenre> findAll() throws SQLException;

    public void insert(String genreName) throws SQLException;

    public MusicalGenre getForName(String genreName) throws SQLException;

}
