package com.tcbook.ws.job;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestAPI;
import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.bean.MusicalGenre;
import com.tcbook.ws.core.bo.MusicalArtistBO;
import com.tcbook.ws.core.bo.MusicalGenreBO;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DataExtractionJob {

    private static final Logger log = LoggerFactory.getLogger(DataExtractionJob.class);
    private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private static MusicalArtistBO musicalArtistBO = new MusicalArtistBO();
    private static MusicalGenreBO musicalGenreBO = new MusicalGenreBO();

    public static void extractData() {
        long before = System.currentTimeMillis();
        try {
            log.info("[DATA_EXTRACTION_JOB] Started DataExtractionJob on {}.", new Date());

            EchoNestAPI echonest = new EchoNestAPI(TCBookProperties.getInstance().getString("echonest.api_key"));
            List<MusicalGenre> listGenres = musicalGenreBO.getAll(); // get all genres

            // filter genre names
            List<String> finalListGenres = new ArrayList<String>();
            for (MusicalGenre genre : listGenres) {
                finalListGenres.add(genre.getName());
            }

            List<MusicalArtist> musicalArtists = musicalArtistBO.getAll();
            for (MusicalArtist artist : musicalArtists) {

                // doesn' need to retrieve data of artists that already have it filled
                if (StringUtils.isBlank(artist.getMbid())) {
                    // extract the artist name from the wikipedia URL
                    String potentialName = artist.getUrl().replace("http://en.wikipedia.org/wiki/", "");
                    String normalizedName = normalize(potentialName);

                    // fill musical artist informations with genres and artistic name
                    String lastFMAPIKey = TCBookProperties.getInstance().getString("lastfm.api_key");
                    extractLastFMData(artist, normalizedName, lastFMAPIKey, finalListGenres, listGenres, echonest);

                    Thread.sleep(200); // avoid to send too much information to external APIs
                }
            }


        } catch (Exception e) {
            log.error("[DATA_EXTRACTION_JOB] Error performing job. Exception: " + e);
            logEx.error("Error performing job.", e);
        } finally {
            log.info("[DATA_EXTRACTION_JOB] Finished DataExtractionJob in {}ms.", (System.currentTimeMillis() - before));
        }
    }

    @SuppressWarnings("unchecked")
	private static void extractLastFMData(MusicalArtist artist, String name, String apiKey, List<String> genreNames, List<MusicalGenre> genres, EchoNestAPI echonest) {
        try {

            // search for artist and parse response
            String responseText = makeArtistSearchRequest(apiKey, name);
            Map<String, Object> responseMap = new ObjectMapper().readValue(responseText, Map.class);
            Map<String, Object> results = (Map<String, Object>) responseMap.get("results");
            Map<String, Object> artistmatches = (Map<String, Object>) results.get("artistmatches");
            Map<String, String> retrievedArtist = (Map<String, String>) artistmatches.get("artist");

            String remoteName = retrievedArtist.get("name");
            String remoteMbid = retrievedArtist.get("mbid");

            if (StringUtils.isBlank(remoteMbid)) {
                // echonest fallback

                try {
                    List<Artist> artists = echonest.searchArtists(name);
                    if (artists != null && artists.size() > 0) {
                        Artist echoNestArtist = artists.get(0);
                        remoteName = echoNestArtist.getName();
                        remoteMbid = echoNestArtist.getForeignID("musicbrainz");
                        if (StringUtils.isNotBlank(remoteMbid)) {
                            remoteMbid = remoteMbid.replace("musicbrainz:artist:", "");
                        }
                    }
                } catch (Exception e) {
                    logEx.error("Error performing EchoNest fallback", e);
                }
            }

            //ensure that all necessary data is found
            if (StringUtils.isNotBlank(remoteName) && StringUtils.isNotBlank(remoteMbid)) {

                artist.setArtisticName(remoteName);
                artist.setMbid(remoteMbid);
                musicalArtistBO.update(artist);

                List<String> artistGenresName = extractGenres(remoteName, remoteMbid, apiKey, genreNames);

                StringBuilder logText = new StringBuilder();
                logText.append("Name -").append(remoteName).append("-, mbid ").append(remoteMbid);
                logText.append(". Searched for: -").append(name).append("-.");
                if (artistGenresName != null) {
                    logText.append("GENRES: ").append(artistGenresName);

                    // retrieve MusicalGenre information from the filtered genre names
                    List<MusicalGenre> artistGenres = new ArrayList<MusicalGenre>();
                    for (String genreName : artistGenresName) {
                        artistGenres.add(findGenreByName(genres, genreName));
                    }

                    musicalArtistBO.setGenresForArtist(artist, artistGenres);
                } else {
                    logText.append("GENRES: NOT FOUND!");
                }
                log.info(logText.toString());
            } else {
                log.error("NOT FOUND. Received: Name -" + remoteName + "-, mbid " + remoteMbid + ". Searched for: -" + name + "-");
            }

        } catch (Exception e) {
            log.error("[DATA_EXTRACTION_JOB] Error extracting LastFM data for artist {}", name);
            logEx.error("Error extracting LastFM data.", e);
        }


    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<String> extractGenres(String name, String mbid, String apiKey, List<String> listGenres) {

        List<String> genres = new ArrayList<String>();

        try {
            String responseText = makeArtistInfoRequest(apiKey, mbid);

            // parse LastFM response
            Map<String, Object> responseMap = new ObjectMapper().readValue(responseText, Map.class);
            Map<String, Object> artist = (Map<String, Object>) responseMap.get("artist");
            Map<String, List> tags = (Map<String, List>) artist.get("tags");
            List<Map> tag = (List<Map>) tags.get("tag");
            for (Map<String, String> tagInfo : tag) {
                // LastFM tags doesn't include only genres, so, it's necessary to validate it using EchoNest genres list
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

    private static String makeArtistInfoRequest(String apiKey, String mbid) {
        String response = null;
        try {
            StringBuilder builder = new StringBuilder("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&format=json&limit=1");
            builder.append("&api_key=").append(apiKey);
            builder.append("&mbid=").append(mbid);


            URL getUrl = new URL(builder.toString());

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
        } catch (Exception e) {
            log.error("[DATA_EXTRACTION_JOB] Error performing getInfo request for LastFM. MBID {}", mbid);
            logEx.error("Error performing LastFM request.", e);
        }
        return response;
    }

    private static String makeArtistSearchRequest(String apiKey, String artistName) {
        String response = null;
        try {
            StringBuilder builder = new StringBuilder("http://ws.audioscrobbler.com/2.0/?method=artist.search&format=json&limit=1");
            builder.append("&api_key=").append(apiKey);
            builder.append("&artist=").append(URLEncoder.encode(artistName, "UTF-8"));


            URL getUrl = new URL(builder.toString());

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
        } catch (Exception e) {
            log.error("[DATA_EXTRACTION_JOB] Error extracting LastFM data for artist {}", artistName);
            logEx.error("Error extracting LastFM data.", e);
        }
        return response;
    }

    private static MusicalGenre findGenreByName(List<MusicalGenre> source, String name) {
        MusicalGenre result = null;
        for (MusicalGenre genre : source) {
            if (genre.getName().equals(name)) {
                result = genre;
                break;
            }
        }
        return result;
    }

    private static String normalize(String s) throws Exception {
        return URLDecoder.decode(s.toLowerCase(), "UTF-8").replaceAll("_\\(.+\\)", "").replaceAll("_", " ");
    }

}
