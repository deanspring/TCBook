package com.tcbook.ws.core.bo;

import com.tcbook.ws.bean.Person;
import com.tcbook.ws.database.dao.ColleagueDAO;
import com.tcbook.ws.database.dao.ColleagueDAOImpl;
import com.tcbook.ws.database.dao.PersonDAO;
import com.tcbook.ws.database.dao.PersonDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caiouvini on 4/9/14.
 */
public class PersonBO {

    private static final Logger logEx = LoggerFactory.getLogger(PersonBO.class);

    private PersonDAO personDAO = PersonDAOImpl.getInstance();
    private ColleagueDAO colleagueDAO = ColleagueDAOImpl.getInstance();

    public List<Person> getAll() {
        return personDAO.findAll();
    }

    public List<Person> getColleaguesForPersonWithId(Long id) {
        List<Long> colleagueIds = colleagueDAO.findAllForId(id);
        List<Person> result = new ArrayList<Person>();

        for (Long personId : colleagueIds) {
            Person p = personDAO.find(personId);
            if (p != null) { //should not happen, but...
                result.add(p);
            }
        }

        return result;
    }

    public Person getPerson(Long id) {
        return personDAO.find(id);
    }

    public void update(Person person) {
        try {
            personDAO.update(person);
        } catch (SQLException e) {
            logEx.error("[TCBook] Error updating person. Exception: " + e);
        }
    }

    public void disable(Long personId) {
        Person p = personDAO.find(personId);
        if (p != null) {
            p.setEnabled(Boolean.FALSE);
            try {
                personDAO.update(p);
            } catch (SQLException e) {
                logEx.error("[TCBook] Error disabling person. Exception: " + e);
            }
        }
    }

}
