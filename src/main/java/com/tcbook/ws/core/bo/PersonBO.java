package com.tcbook.ws.core.bo;

import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.bean.Person;
import com.tcbook.ws.database.dao.*;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

public class PersonBO {

	private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

	private PersonDAO personDAO = PersonDAOImpl.getInstance();
	private ColleagueDAO colleagueDAO = ColleagueDAOImpl.getInstance();
	private PersonLikeMusicalArtistDAO personLikeMusicalArtistDAO = PersonLikeMusicalArtistDAOImpl.getInstance();
	private MusicalArtistGenresDAO musicalArtistGenresDAO = MusicalArtistGenresDAOImpl.getInstance();
	private MusicalArtistDAO musicalArtistDAO = MusicalArtistDAOImpl.getInstance();

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

		Map<Long, Double> artistsSelectedToBeRecommended = new LinkedHashMap<Long, Double>();

		artistsSelectedToBeRecommended = recommendationStep1(personId, artistsSelectedToBeRecommended);
		artistsSelectedToBeRecommended = recommendationStep2(artistsSelectedToBeRecommended);
		artistsSelectedToBeRecommended = recommendationStep3(personId, artistsSelectedToBeRecommended);

		Integer maxRecommendationResults = TCBookProperties.getInstance().getInt("tcbook.recommendation.max_results", 10);

		int i = 0;
		List<MusicalArtist> result = new ArrayList<MusicalArtist>();
		for (Map.Entry<Long, Double> entry : artistsSelectedToBeRecommended.entrySet()) {
			if (i >= maxRecommendationResults) {
				break;
			}

			result.add(musicalArtistDAO.find(entry.getKey()));
			i++;
		}

		return result;
	}

	private Map<Long, Double> recommendationStep1(Long personId, Map<Long, Double> artistsSelectedToBeRecommended) {

		Map<Long, Double> result = null;

		Double step1Weight = TCBookProperties.getInstance().getDouble("tcbook.recommendation.step1.weight", 0.5);
		Integer minimumRate = TCBookProperties.getInstance().getInt("tcbook.recommendation.step1.minimum_rate", 3);
		Integer step1MaxResults = TCBookProperties.getInstance().getInt("tcbook.recommendation.step1.max_results", 20);

		// retrieve the preferred artists
		List<Long> artists = personLikeMusicalArtistDAO.artistsForPersonWithMinimumRate(personId, minimumRate);
		Set<Long> artistsLikedByPerson = null;
		try {
			Map<Long, Integer> artistsLikedMap = personLikeMusicalArtistDAO.getAllForPerson(personId);
			artistsLikedByPerson = artistsLikedMap.keySet();
		} catch (Exception e) {
			logEx.error("[PERSON_BO] Error searching all artists that a person like.", e);
		}

		if ((artists != null && !artists.isEmpty()) && artistsLikedByPerson != null) {

			// will keep the score that each artist will obtain
			Map<Long, Double> pointsByArtist = new HashMap<Long, Double>();

			// keep the genres of the preferred artists, keep also the amount of
			// times that the genre entry appear
			Map<Long, Integer> occurrencesByGenre = musicalArtistGenresDAO.getGroupedGenresForArtists(artists);

			Integer totalOccurrences = 0; // all the genres appearances
			for (Integer occurrence : occurrencesByGenre.values()) {
				totalOccurrences += occurrence;
			}

			// look for artists in order to see who best fits into the retrieved
			// genres
			List<MusicalArtist> allArtists = musicalArtistDAO.findAll();
			for (MusicalArtist artist : allArtists) {

				if (!artistsLikedByPerson.contains(artist.getId())) { // doesn't
																		// make
																		// sense
																		// try
																		// to
																		// recommend
																		// an
																		// artist
																		// that
																		// the
																		// given
																		// person
																		// already
																		// like
					int points = 0; // points scored by the current artist
					List<Long> genres = musicalArtistGenresDAO.findGenresByArtist(artist.getId());
					for (Long genreId : genres) {
						if (occurrencesByGenre.get(genreId) != null) {
							points += occurrencesByGenre.get(genreId); // the
																		// current
																		// artist
																		// has a
																		// genre
																		// equals
																		// than
																		// the
																		// preferred
																		// ones
						}
					}
					Double artistPontuation = (step1Weight * points) / totalOccurrences; // compute
																							// the
																							// score
					pointsByArtist.put(artist.getId(), artistPontuation); // keep
																			// it
																			// to
																			// post
																			// processing
				}

			}

			// order the artists taking it's scores as reference
			result = orderArtistsToBeRecommendedScores(pointsByArtist, step1MaxResults);
		}

		return result != null ? result : artistsSelectedToBeRecommended;
	}

	private Map<Long, Double> recommendationStep2(Map<Long, Double> artistsSelectedToBeRecommended) {

		Map<Long, Double> result = null;

		if (artistsSelectedToBeRecommended != null && !artistsSelectedToBeRecommended.isEmpty()) {
			Double step2Weight = TCBookProperties.getInstance().getDouble("tcbook.recommendation.step2.weight", 0.3);

			// retrieve the rating averages of the selected artists
			Map<Long, Double> averagesByArtist = personLikeMusicalArtistDAO.averagesForArtists(new ArrayList<Long>(artistsSelectedToBeRecommended.keySet()));

			for (Map.Entry<Long, Double> entry : averagesByArtist.entrySet()) {
				Double currentScore = artistsSelectedToBeRecommended.get(entry.getKey());
				currentScore += (step2Weight * entry.getValue()) / 5; // update
																		// the
																		// artists
																		// scores
																		// (5 is
																		// the
																		// maximum
																		// rating)
				artistsSelectedToBeRecommended.put(entry.getKey(), currentScore);
			}

			result = orderArtistsToBeRecommendedScores(artistsSelectedToBeRecommended);
		}

		return result != null ? result : artistsSelectedToBeRecommended;

	}

	private Map<Long, Double> recommendationStep3(Long personId, Map<Long, Double> artistsSelectedToBeRecommended) {

		Map<Long, Double> result = null;

		if (artistsSelectedToBeRecommended != null && !artistsSelectedToBeRecommended.isEmpty()) {
			Double step3Weight = TCBookProperties.getInstance().getDouble("tcbook.recommendation.step3.weight", 0.2);

			// retrieve the amount of likes each artist received from friends of
			// the given person
			Map<Long, Integer> likesByArtist = personLikeMusicalArtistDAO.artistsLikesByFriends(new ArrayList<Long>(artistsSelectedToBeRecommended.keySet()), personId);

			if (likesByArtist != null && !likesByArtist.isEmpty()) {
				// find the max amount of times an artist is rated by friends of
				// the given person
				Integer maxOccurrences = -1;
				for (Map.Entry<Long, Integer> entry : likesByArtist.entrySet()) {
					if (entry.getValue() > maxOccurrences) {
						maxOccurrences = entry.getValue();
					}
				}

				for (Map.Entry<Long, Integer> entry : likesByArtist.entrySet()) {
					Double currentScore = artistsSelectedToBeRecommended.get(entry.getKey());
					currentScore += (step3Weight * entry.getValue()) / maxOccurrences; // update
																						// the
																						// artists
																						// scores
					artistsSelectedToBeRecommended.put(entry.getKey(), currentScore);
				}

				result = orderArtistsToBeRecommendedScores(artistsSelectedToBeRecommended);
			}
		}

		return result != null ? result : artistsSelectedToBeRecommended;
	}

	private Map<Long, Double> orderArtistsToBeRecommendedScores(Map<Long, Double> pointsByArtist) {
		return orderArtistsToBeRecommendedScores(pointsByArtist, -1);
	}

	private Map<Long, Double> orderArtistsToBeRecommendedScores(Map<Long, Double> pointsByArtist, int maxResults) {
		List<Map.Entry<Long, Double>> pointsEntries = new ArrayList<Map.Entry<Long, Double>>(pointsByArtist.entrySet());
		Collections.sort(pointsEntries, new Comparator<Map.Entry<Long, Double>>() {
			public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
				return o2.getValue().compareTo(o1.getValue()); // desc
			}
		});

		Map<Long, Double> result = new LinkedHashMap<Long, Double>();

		if (maxResults > -1) {
			int i = 0;
			for (Map.Entry<Long, Double> entry : pointsEntries) {
				if (i == maxResults) { // select only the maxResults first entries
					break;
				}

				result.put(entry.getKey(), entry.getValue());
				i++;
			}
		} else {
			for (Map.Entry<Long, Double> entry : pointsEntries) { // insert all ordered values
				result.put(entry.getKey(), entry.getValue());
			}
		}

		return result;

	}

}
