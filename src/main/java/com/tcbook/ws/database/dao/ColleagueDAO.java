package com.tcbook.ws.database.dao;

import java.sql.SQLException;
import java.util.List;

public interface ColleagueDAO {

    public List<Long> findAllForId(Long id);

    public void insert(Long idPerson, Long idColleague) throws SQLException;

    public void remove(Long idPerson, Long idColleague) throws SQLException;

}
