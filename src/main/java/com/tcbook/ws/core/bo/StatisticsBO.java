package com.tcbook.ws.core.bo;

import com.tcbook.ws.bean.*;
import com.tcbook.ws.database.dao.*;
import com.tcbook.ws.util.MusicalArtistUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatisticsBO {

	private static PersonLikeMusicalArtistDAO personLikeMusicalArtistDAO = PersonLikeMusicalArtistDAOImpl.getInstance();
	private static MusicalArtistDAO musicalArtistDAO = MusicalArtistDAOImpl.getInstance();
	private static MusicalArtistGenresDAO musicalArtistGenresDAO = MusicalArtistGenresDAOImpl.getInstance();
	private static MusicalGenresDAO musicalGenresDAO = MusicalGenresDAOImpl.getInstance();
	private static ColleagueDAO colleagueDAO = ColleagueDAOImpl.getInstance();
	private static PersonDAO personDAO = PersonDAOImpl.getInstance();
	private static RegionDAO regionDAO = RegionDAOImpl.getInstance();
	private static CountryDAO countryDAO = CountryDAOImpl.getInstance();

	public Map<String, Float> getGeneralRatings() {
		return personLikeMusicalArtistDAO.generalRatings();
	}

	public Map<String, Float> getAverageByArtist() {
		return fromArtistIdToNameFloatMap(personLikeMusicalArtistDAO.averageByArtist());
	}

	public Map<String, Float> getTopTwentyAveragesByArtist() {
		return fromArtistIdToNameFloatMap(personLikeMusicalArtistDAO.topTwentyAveragesByArtist());
	}

	public Map<String, Integer> getTopTenPopularArtists() {
		return fromArtistIdToNameIntegerMap(personLikeMusicalArtistDAO.topTenPopularArtists());
	}

	public Map<String, Float> getTopTenStandardDeviationByArtist() {
		return fromArtistIdToNameFloatMap(personLikeMusicalArtistDAO.topTenStandardDeviationByArtist());
	}

	public Map<String, Integer> getTopFivePopularGenres() {
		return fromGenreIdToNameIntegerMap(musicalArtistGenresDAO.topFivePopularGenres());
	}

	public Map<String, Integer> getTopTenKnownWithMostSharedArtists() {
		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		List<ColleagueMusicalArtistSharingInfo> listInfo = colleagueDAO.topTenKnownWithMostSharedArtists();

		for (ColleagueMusicalArtistSharingInfo info : listInfo) {
			String personName = personNameFromId(info.getIdPerson());
			String colleagueName = personNameFromId(info.getIdColleague());
			result.put(personName + "->" + colleagueName, info.getAmount());
		}

		return result;
	}

	public Map<String, Double> getArtistsPopularityQuartis() {
		return personLikeMusicalArtistDAO.artistsPopularityQuartis();
	}

	public Map<Long, Integer> getArtistsPopularity() {
		return personLikeMusicalArtistDAO.artistsPopularity();
	}

	public Map<Integer, Integer> getPeopleLikesByAmount() {
		return personLikeMusicalArtistDAO.peopleLikesByAmount();
	}

	public Map<Integer, Integer> getArtistsLikesByAmount() {
		return personLikeMusicalArtistDAO.artistsLikesByAmount();
	}

	public Map<String, Integer> getTopTenCountriesWithMostArtists() {
		Map<String, Integer> result = new LinkedHashMap<String, Integer>();

		Map<Long, Integer> topTenInfo = regionDAO.topTenCountriesWithMostArtists();
		for (Map.Entry<Long, Integer> info : topTenInfo.entrySet()) {
			result.put(countryNameFromId(info.getKey()), info.getValue());
		}

		return result;
	}

	public Map<String, Integer> getTopTenEclecticPeople() {
		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		Map<Long, Integer> topTenMap = personDAO.topTenEclecticPeople();

		for (Map.Entry<Long, Integer> entry : topTenMap.entrySet()) {
			result.put(personNameFromId(entry.getKey()), entry.getValue());
		}

		return result;
	}

	private Map<String, Float> fromArtistIdToNameFloatMap(Map<Long, Float> artistInfoById) {
		Map<String, Float> result = new LinkedHashMap<String, Float>();
		for (Map.Entry<Long, Float> entry : artistInfoById.entrySet()) {
			String name = artistNameFromId(entry.getKey());
			result.put(name, entry.getValue());
		}

		return result;
	}

	private Map<String, Integer> fromArtistIdToNameIntegerMap(Map<Long, Integer> artistInfoById) {
		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		for (Map.Entry<Long, Integer> entry : artistInfoById.entrySet()) {
			String name = artistNameFromId(entry.getKey());
			result.put(name, entry.getValue());
		}

		return result;
	}

	private String artistNameFromId(Long musicalArtistId) {
		MusicalArtist artist = musicalArtistDAO.find(musicalArtistId);
		String name = artist.getArtisticName();
		if (StringUtils.isBlank(name)) {
			try {
				name = MusicalArtistUtils.getArtistNameFromWikipediaURLNormalized(artist);
			} catch (Exception e) {
				name = "Unavailable";
			}
		}
		return name;
	}

	private Map<String, Integer> fromGenreIdToNameIntegerMap(Map<Long, Integer> genreInfoById) {
		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		for (Map.Entry<Long, Integer> entry : genreInfoById.entrySet()) {
			String name = genreNameFromId(entry.getKey());
			result.put(name, entry.getValue());
		}

		return result;
	}

	private String genreNameFromId(Long genreId) {
		MusicalGenre genre = musicalGenresDAO.find(genreId);
		String name = genre.getName();
		if (StringUtils.isBlank(name)) {
			name = "Unavailable";
		}
		return name;
	}

	private String personNameFromId(Long personId) {
		Person person = personDAO.find(personId);
		String name = person.getName();
		if (StringUtils.isBlank(name)) {
			name = "Unavailable";
		}
		return name;
	}

	private String countryNameFromId(Long countryId) {
		Country country = countryDAO.findForID(countryId);
		String name = country.getName();
		if (StringUtils.isBlank(name)) {
			name = "Unavailable";
		}
		return name;
	}

}
