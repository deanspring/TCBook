package com.tcbook.ws.core.bo;

import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.bean.Person;
import com.tcbook.ws.database.dao.*;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonBO {

    private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private PersonDAO personDAO = PersonDAOImpl.getInstance();
    private ColleagueDAO colleagueDAO = ColleagueDAOImpl.getInstance();
    private PersonLikeMusicalArtistDAO personLikeMusicalArtistDAO = PersonLikeMusicalArtistDAOImpl.getInstance();

    public List<Person> getAll() {
        List<Person> people = personDAO.findAll();
        return people == null ? new ArrayList<Person>() : people;
    }

    public void setColleaguesForPersonWithId(Long personId, List<Long> newColleaguesIds) {
        removeOldColleagues(personId);
        addNewColleagues(personId, newColleaguesIds);
    }

    protected void addNewColleagues(Long personId, List<Long> newColleaguesIds) {
        if (newColleaguesIds == null)
            return;

        for (Long colleagueId : newColleaguesIds)
            try {
                colleagueDAO.insert(personId, colleagueId);
            } catch (SQLException e) {
                logEx.error("[TCBook] Error inserting colleague. Exception: " + e);
                e.printStackTrace();
            }
    }

    protected void removeOldColleagues(Long personId) {
        List<Long> colleagueIds = colleagueDAO.findAllForId(personId);

        if (colleagueIds == null)
            return;

        for (Long colleagueId : colleagueIds)
            try {
                colleagueDAO.remove(personId, colleagueId);
            } catch (SQLException e) {
                logEx.error("[TCBook] Error removing colleague. Exception: " + e);
                e.printStackTrace();
            }
    }

    public List<Person> getColleaguesForPersonWithId(Long id) {
        List<Long> colleagueIds = colleagueDAO.findAllForId(id);
        List<Person> result = new ArrayList<Person>();

        if (colleagueIds == null)
            return result;

        for (Long colleagueId : colleagueIds) {
            Person p = personDAO.find(colleagueId);
            if (p != null) { // should not happen, but...
                result.add(p);
            }
        }

        return result;
    }

    public Person getPerson(Long id) {
        Person person = personDAO.find(id);
        return person == null ? new Person() : person;
    }

    public void save(Person person) {
        if (person == null)
            return;

        try {
            if (person.getId() != null) {
                Person oldPerson = getPerson(person.getId());
                if (oldPerson.getId() == null)
                    return;
                oldPerson.setHometown(person.getHometown());
                oldPerson.setLogin(person.getLogin());
                oldPerson.setName(person.getName());
                oldPerson.setUrl(person.getUrl());

                personDAO.update(oldPerson);
            } else {
                person.setEnabled(true);
                personDAO.insert(person);
            }
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

    public void setLikes(Long personId, Map<Long, Integer> likes) {
        Map<Long, Integer> existingLikes = null;

        try {
            existingLikes = personLikeMusicalArtistDAO.getAllForPerson(personId);
        } catch (SQLException e) {
            logEx.error("[TCBook] Error retrieving person's like. Exception: " + e);
        }

        for (Map.Entry<Long, Integer> entry : likes.entrySet()) {
            try {
                if (existingLikes != null && existingLikes.containsKey(entry.getKey())) {
                    personLikeMusicalArtistDAO.updateLike(personId, entry.getKey(), entry.getValue());
                } else {
                    personLikeMusicalArtistDAO.insert(personId, entry.getKey(), entry.getValue());
                }
            } catch (SQLException e) {
                logEx.error("[TCBook] Error registering person's like. Exception: " + e);
            }
        }
    }

    public Map<Long, Integer> getMusicalsArtistsForPersonWithId(Long personId) {
        try {
            return personLikeMusicalArtistDAO.getAllForPerson(personId);
        } catch (Exception e) {
            logEx.error("[TCBook] Error retrieving musicalArtists that person {} like. Exception: " + e, personId);

            return new HashMap<Long, Integer>();
        }
    }

    public List<MusicalArtist> recommendArtistsForPerson(Long personId) {
        //TODO
        return null;
    }

}
