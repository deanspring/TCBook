package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.Person;

import java.sql.SQLException;
import java.util.List;

public interface PersonDAO {

    public Person find(Long id);

    public List<Person> findAll();

    public void insert(Person person) throws SQLException;

    public void remove(Long id) throws SQLException;

    public void update(Person person) throws SQLException;

    public void removeColleaguesForPerson(Long idPerson) throws SQLException;

    public void removeBlockingsForPerson(Long idPerson) throws SQLException;

}
