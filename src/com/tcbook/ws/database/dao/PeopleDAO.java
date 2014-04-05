package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.People;

import java.sql.SQLException;
import java.util.List;

public interface PeopleDAO {

    public People find(Long id);

    public List<People> findAll();

    public void insert(People people) throws SQLException;

    public void remove(Long id) throws SQLException;

    public void update(People people) throws SQLException;

    public void removeColleaguesForPeople(Long idPeople) throws SQLException;

    public void removeBlockingsForPeople(Long idPeople) throws SQLException;

}
