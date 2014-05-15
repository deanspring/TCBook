package com.tcbook.ws.web.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.bean.MusicalGenre;
import com.tcbook.ws.database.dao.CityDAO;
import com.tcbook.ws.database.dao.CityDAOImpl;
import com.tcbook.ws.database.dao.CountryDAO;
import com.tcbook.ws.database.dao.CountryDAOImpl;
import com.tcbook.ws.database.dao.MusicalArtistDAO;
import com.tcbook.ws.database.dao.MusicalArtistDAOImpl;
import com.tcbook.ws.util.TCBookConstants;

@Path("/musicalArtist")
public class MusicalArtistRS {

	private static Logger logReqAnswered = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_REQUESTS_ANSWERED);

	private MusicalArtistDAO musicalArtistDAO = MusicalArtistDAOImpl.getInstance();
	private CityDAO cityDAO = CityDAOImpl.getInstance();
	private CountryDAO countryDAO = CountryDAOImpl.getInstance();

	@GET
	@Path("/")
	public Response getAll() {
		logReqAnswered.info("list of musicals artists.");

		return Response.ok(toJson(musicalArtistDAO.findAll())).build();
	}

	protected String toJson(List<MusicalArtist> artists) {
		JsonArray artistsJson = new JsonArray();

		Map<Long, String> cities = new HashMap<Long, String>();
		Map<Long, String> countries = new HashMap<Long, String>();

		for (MusicalArtist artist : artists) {
			getRegionInfo(cities, countries, artist);

			artistsJson.add(getArtistAsJson(cities, countries, artist));
		}

		
		return artistsJson.toString();
	}

	protected JsonObject getArtistAsJson(Map<Long, String> cities, Map<Long, String> countries, MusicalArtist artist) {
		JsonObject artistJson = new JsonObject();

		artistJson.addProperty("id", artist.getId());
		artistJson.addProperty("artisticName", artist.getArtisticName());
		artistJson.addProperty("city", cities.get(artist.getIdRegion()));
		artistJson.addProperty("country", countries.get(artist.getIdRegion()));
		artistJson.addProperty("url", artist.getUrl());
		artistJson.add("genres", getGenresFromArtists(artist.getId()));
		return artistJson;
	}

	protected void getRegionInfo(Map<Long, String> cities, Map<Long, String> countries, MusicalArtist artist) {
		if (artist.getIdRegion() == null)
			return;

		if (!cities.containsKey(artist.getIdRegion())) {
			cities.put(artist.getIdRegion(), cityDAO.findByRegion(artist.getIdRegion()).getName());
		}
		if (!countries.containsKey(artist.getIdRegion())) {
			countries.put(artist.getIdRegion(), countryDAO.findByRegion(artist.getIdRegion()).getName());
		}
	}

	protected JsonArray getGenresFromArtists(Long artistId) {
		List<MusicalGenre> genres = musicalArtistDAO.getGenresOfArtist(artistId);

		JsonArray genresJson = new JsonArray();
		for (MusicalGenre musicalGenre : genres)
			genresJson.add(new JsonPrimitive(musicalGenre.getName()));

		return genresJson;
	}

}
