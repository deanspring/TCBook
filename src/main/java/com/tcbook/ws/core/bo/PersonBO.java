package com.tcbook.ws.core.bo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcbook.ws.bean.Person;
import com.tcbook.ws.database.dao.ColleagueDAO;
import com.tcbook.ws.database.dao.ColleagueDAOImpl;
import com.tcbook.ws.database.dao.PersonDAO;
import com.tcbook.ws.database.dao.PersonDAOImpl;
import com.tcbook.ws.database.dao.PersonLikeMusicalArtistDAO;
import com.tcbook.ws.database.dao.PersonLikeMusicalArtistDAOImpl;

public class PersonBO {

	private static final Logger logEx = LoggerFactory.getLogger(PersonBO.class);

	private PersonDAO personDAO = PersonDAOImpl.getInstance();
	private ColleagueDAO colleagueDAO = ColleagueDAOImpl.getInstance();
	private PersonLikeMusicalArtistDAO personLikeMusicalArtistDAO = PersonLikeMusicalArtistDAOImpl.getInstance();

	public List<Person> getAll() {
		List<Person> people = personDAO.findAll();
		return people == null ? new ArrayList<Person>() : people;
	}

	public List<Person> getColleaguesForPersonWithId(Long id) {
		List<Long> colleagueIds = colleagueDAO.findAllForId(id);
		List<Person> result = new ArrayList<Person>();

		for (Long personId : colleagueIds) {
			Person p = personDAO.find(personId);
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
		for (Map.Entry<Long, Integer> entry : likes.entrySet()) {
			try {
				personLikeMusicalArtistDAO.insert(personId, entry.getKey(), entry.getValue());
			} catch (SQLException e) {
				logEx.error("[TCBook] Error registering person's like. Exception: " + e);
			}
		}
	}

}
