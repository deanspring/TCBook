package com.tcbook.ws.job;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.tcbook.ws.bean.*;
import com.tcbook.ws.core.bo.MusicalArtistBO;
import com.tcbook.ws.core.bo.MusicalGenreBO;
import com.tcbook.ws.core.bo.RegionBO;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DataExtractionJob {

	private static final String LASTFM_BASE_URL = "http://ws.audioscrobbler.com/2.0/?format=json&limit=1";
	private static final Logger log = LoggerFactory.getLogger(DataExtractionJob.class);
	private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

	private static MusicalArtistBO musicalArtistBO = new MusicalArtistBO();
	private static MusicalGenreBO musicalGenreBO = new MusicalGenreBO();
	private static RegionBO regionBO = new RegionBO();

	public static void extractData() {
		long before = System.currentTimeMillis();

		try {
			log.info("[DATA_EXTRACTION_JOB] Started DataExtractionJob on {}.", new Date());

			EchoNestAPI echonest = new EchoNestAPI(TCBookProperties.getInstance().getString("echonest.api_key"));

			List<MusicalArtist> musicalArtists = musicalArtistBO.getAll();
			for (MusicalArtist artist : musicalArtists) {

				String artistName = getArtistNameFromWikipediaURLNormalized(artist);

				if (!hasAlreadyRetrivenDataOfTheArtist(artist)) {
					retriveDataOfTheArtist(echonest, artist, artistName);
				}

				if (artist.getIdRegion() == null) {
					extractRegionData(echonest, StringUtils.isNotBlank(artist.getArtisticName()) ? artist.getArtisticName() : artistName, artist);
				}
			}

		} catch (Exception e) {
			log.error("[DATA_EXTRACTION_JOB] Error performing job. Exception: " + e);
			logEx.error("Error performing job.", e);
		} finally {
			log.info("[DATA_EXTRACTION_JOB] Finished DataExtractionJob in {}ms.", (System.currentTimeMillis() - before));
		}
	}

	protected static void retriveDataOfTheArtist(EchoNestAPI echonest, MusicalArtist artist, String artistName)
			throws InterruptedException {
		String lastFMAPIKey = TCBookProperties.getInstance().getString("lastfm.api_key");
		extractLastFMData(artist, artistName, lastFMAPIKey, echonest);

		// avoid sending too much information to external APIs
		Thread.sleep(200);
	}

	protected static boolean hasAlreadyRetrivenDataOfTheArtist(MusicalArtist artist) {
		return StringUtils.isNotBlank(artist.getMbid());
	}

	protected static String getArtistNameFromWikipediaURLNormalized(MusicalArtist artist) throws Exception {
		return normalize(artist.getUrl().replace("http://en.wikipedia.org/wiki/", ""));
	}

	private static void extractLastFMData(MusicalArtist artist, String artistName, String apiKey, EchoNestAPI echonest) {
		try {

			Map<String, String> retrievedArtist = getArtistFromLastFMApi(artistName, apiKey);

			String remoteName = retrievedArtist.get("name");
			String remoteMbid = retrievedArtist.get("mbid");

			if (!foundArtist(remoteMbid)) {
				// echonest fallback
				Artist echoNestArtist = getArtistFromEchoNestAPI(artistName, echonest);

				if (echoNestArtist != null) {
					remoteName = echoNestArtist.getName();
					remoteMbid = echoNestArtist.getForeignID("musicbrainz");
					if (StringUtils.isNotBlank(remoteMbid)) {
						remoteMbid = remoteMbid.replace("musicbrainz:artist:", "");
					}
				}
			}

			// ensure that all necessary data is found
			if (StringUtils.isBlank(remoteName) || StringUtils.isBlank(remoteMbid)) {
				log.error("NOT FOUND. Received: Name -" + remoteName + "-, mbid " + remoteMbid + ". Searched for: -" + artistName + "-");
			} else {
				updateArtistFromDB(artist, artistName, apiKey, remoteName, remoteMbid);
			}

		} catch (Exception e) {
			log.error("[DATA_EXTRACTION_JOB] Error extracting LastFM data for artist {}", artistName);
			logEx.error("Error extracting LastFM data.", e);
		}

	}

	protected static void updateArtistFromDB(MusicalArtist artist, String name, String apiKey, String remoteName, String remoteMbid)
			throws Exception {
		artist.setArtisticName(remoteName);
		artist.setMbid(remoteMbid);
		musicalArtistBO.update(artist);

		updateGenderFromArtist(artist, name, apiKey, remoteName, remoteMbid);
	}

	protected static void updateGenderFromArtist(MusicalArtist artist, String name, String apiKey, String remoteName, String remoteMbid) {
		List<MusicalGenre> genres = musicalGenreBO.getAll();

		List<String> artistGenresName = extractGenres(remoteName, remoteMbid, apiKey, musicalGenreBO.getAllNamesFromList(genres));

		StringBuilder logText = new StringBuilder();
		logText.append("Name -").append(remoteName).append("-, mbid ").append(remoteMbid);
		logText.append(". Searched for: -").append(name).append("-.");

		if (artistGenresName == null) {
			logText.append("GENRES: NOT FOUND!");
		} else {
			logText.append("GENRES: ").append(artistGenresName);

			// retrieve MusicalGenre information from the filtered genre names
			List<MusicalGenre> artistGenres = new ArrayList<MusicalGenre>();
			for (String genreName : artistGenresName) {
				artistGenres.add(findGenreByName(genres, genreName));
			}

			musicalArtistBO.setGenresForArtist(artist, artistGenres);
		}

		log.info(logText.toString());
	}

	protected static Artist getArtistFromEchoNestAPI(String name, EchoNestAPI echonest) {
		try {
			boolean extracted = false;

			while (!extracted) {
				try {
					List<Artist> artists = echonest.searchArtists(name);

					if (artists != null && artists.size() > 0) {
						return artists.get(0);
					}

					extracted = true;
				} catch (EchoNestException e) {
					log.error("Echonest limit exceeded. will retry.");
					try {
						Thread.sleep(10000);
					} catch (Exception e1) {
						// do nothing
					}
				}
			}
		} catch (Exception e) {
			logEx.error("Error performing EchoNest artists search", e);
		}

		return null;
	}

	protected static boolean foundArtist(String remoteMbid) {
		return StringUtils.isNotBlank(remoteMbid);
	}

	@SuppressWarnings("unchecked")
	protected static Map<String, String> getArtistFromLastFMApi(String name, String apiKey) throws IOException, JsonParseException, JsonMappingException {
		String responseText = makeArtistSearchRequest(apiKey, name);

		Map<String, Object> responseMap = new ObjectMapper().readValue(responseText, Map.class);
		Map<String, Object> results = (Map<String, Object>) responseMap.get("results");
		Map<String, Object> artistmatches = (Map<String, Object>) results.get("artistmatches");

		return (Map<String, String>) artistmatches.get("artist");
	}

	@SuppressWarnings("rawtypes")
	private static List<String> extractGenres(String name, String mbid, String apiKey, List<String> listGenres) {
		List<String> genres = new ArrayList<String>();

		try {
			List<Map> tag = getGenrerTagsFromLastFMApi(mbid, apiKey);

			for (Map<String, String> tagInfo : tag) {
				// LastFM tags doesn't include only genres, so, it's necessary
				// to validate it using EchoNest genres list
				if (listGenres.contains(tagInfo.get("name").toLowerCase())) {
					genres.add(tagInfo.get("name").toLowerCase());
				}
			}
		} catch (Exception e) {
			log.error("[DATA_EXTRACTION_JOB] Error parsing genres for artist {}", name);
			logEx.error("Error parsing genres.", e);
		}
		return genres;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static List<Map> getGenrerTagsFromLastFMApi(String mbid, String apiKey) throws IOException, JsonParseException, JsonMappingException {
		String responseText = makeArtistInfoRequest(apiKey, mbid);

		Map<String, Object> responseMap = new ObjectMapper().readValue(responseText, Map.class);
		Map<String, Object> artist = (Map<String, Object>) responseMap.get("artist");
		Map<String, List> tags = (Map<String, List>) artist.get("tags");

		return (List<Map>) tags.get("tag");
	}

	private static void extractRegionData(EchoNestAPI echonest, String remoteName, MusicalArtist artist) {
		StringBuilder logText = new StringBuilder("Region for artist ").append(remoteName).append(": ");

		Artist echonestArtist = getArtistFromEchoNestAPI(remoteName, echonest);

		if (echonestArtist == null)
			logText.append("NOT FOUND");
		else {
			createRegionAndUpdateArtist(artist, logText, echonestArtist);
		}

		log.info(logText.toString());
	}

	protected static void createRegionAndUpdateArtist(MusicalArtist artist, StringBuilder logText, Artist echonestArtist) {
		try {
			String remoteCity = echonestArtist.getArtistLocation().getCity();
			String remoteCountry = echonestArtist.getArtistLocation().getCountry();

			if (StringUtils.isBlank(remoteCity) && StringUtils.isBlank(remoteCountry)) {
				return;
			}

			City city = new City();
			city.setName(remoteCity);

			Country country = new Country();
			country.setName(remoteCountry);

			regionBO.createRegion(city, country);

			Region region = regionBO.getRegion(city.getName(), country.getName());

			artist.setIdRegion(region.getId());
			musicalArtistBO.update(artist);

			logText.append("FOUND id ").append(region.getId());
			logText.append(" for city (").append(region.getIdCity()).append(", ").append(remoteCity).append(")");
			logText.append(" for country (").append(region.getIdCountry()).append(", ").append(remoteCountry).append(")");
		} catch (Exception e) {
			logEx.error("Error extracting region for artist " + artist);
		}
	}

	private static String makeArtistInfoRequest(String apiKey, String mbid) {
		try {
			return makeHttpGETRequest(getArtistInfoRequestURL(apiKey, mbid));
		} catch (Exception e) {
			log.error("[DATA_EXTRACTION_JOB] Error performing getInfo request for LastFM. MBID {}", mbid);
			logEx.error("Error performing LastFM request.", e);

			return null;
		}
	}

	private static String makeArtistSearchRequest(String apiKey, String artistName) {
		try {
			return makeHttpGETRequest(getArtistSearchRequestURL(apiKey, artistName).toString());
		} catch (Exception e) {
			log.error("[DATA_EXTRACTION_JOB] Error extracting LastFM data for artist {}", artistName);
			logEx.error("Error extracting LastFM data.", e);

			return null;
		}
	}

	private static MusicalGenre findGenreByName(List<MusicalGenre> source, String name) {
		MusicalGenre musicalGenre = null;

		for (MusicalGenre genre : source) {
			if (genre.getName().equals(name)) {
				musicalGenre = genre;
				break;
			}
		}

		return musicalGenre;
	}

	protected static String makeHttpGETRequest(String url) throws MalformedURLException, IOException, ProtocolException {
		String response;
		URL getUrl = new URL(url);

		HttpURLConnection con = (HttpURLConnection) getUrl.openConnection();

		con.setRequestMethod("GET");
		con.getResponseCode();

		InputStream is = con.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String resultLine = null;
		StringBuilder result = new StringBuilder();
		while ((resultLine = br.readLine()) != null) {
			result.append(resultLine).append("\r\n");
		}
		br.close();

		response = result.toString();
		return response;
	}

	protected static String getArtistInfoRequestURL(String apiKey, String mbid) {
		StringBuilder builder = new StringBuilder(LASTFM_BASE_URL);

		builder.append("&method=artist.getinfo");
		builder.append("&api_key=").append(apiKey);
		builder.append("&mbid=").append(mbid);

		return builder.toString();
	}

	protected static String getArtistSearchRequestURL(String apiKey, String artistName) throws UnsupportedEncodingException {
		StringBuilder builder = new StringBuilder(LASTFM_BASE_URL);

		builder.append("&method=artist.search");
		builder.append("&api_key=").append(apiKey);
		builder.append("&artist=").append(URLEncoder.encode(artistName, "UTF-8"));

		return builder.toString();
	}

	private static String normalize(String s) throws Exception {
		return URLDecoder.decode(s.toLowerCase(), "UTF-8").replaceAll("_\\(.+\\)", "").replaceAll("_", " ");
	}

}
