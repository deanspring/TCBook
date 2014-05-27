package com.tcbook.ws.web.rest;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tcbook.ws.core.bo.StatisticsBO;
import com.tcbook.ws.util.TCBookConstants;

@Path("/statistics")
public class StatisticsRS {

	private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static StatisticsBO statisticsBO = new StatisticsBO();

	// 1
	@GET
	@Path("general_ratings")
	public Response generalRatings() {
		Map<String, Float> generalRatings = statisticsBO.getGeneralRatings();

		try {
			return Response.ok(MAPPER.writeValueAsString(generalRatings)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading general ratings " + generalRatings + " as json.", e);
			return Response.serverError().build();
		}
	}

	// 2
	@GET
	@Path("average_by_artist")
	public Response averageByArtist() {
		Map<String, Float> averageByArtist = statisticsBO.getAverageByArtist();

		try {
			return Response.ok(MAPPER.writeValueAsString(averageByArtist)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading average ratings by artist" + averageByArtist + " as json.", e);
			return Response.serverError().build();
		}
	}

	// 3
	@GET
	@Path("top_twenty_averages_by_artist")
	public Response topTwentyAveragesByArtist() {
		Map<String, Float> topTwentyAveragesByArtist = statisticsBO.getTopTwentyAveragesByArtist();

		try {
			return Response.ok(MAPPER.writeValueAsString(topTwentyAveragesByArtist)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading top twenty average ratings by artist " + topTwentyAveragesByArtist + " as json.", e);
			return Response.serverError().build();
		}
	}

	// 4
	@GET
	@Path("top_ten_popular_artists")
	public Response topTenPopularArtists() {
		Map<String, Integer> topTenPopularArtists = statisticsBO.getTopTenPopularArtists();

		try {
			return Response.ok(MAPPER.writeValueAsString(topTenPopularArtists)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading top ten popular artists " + topTenPopularArtists + " as json.", e);
			return Response.serverError().build();
		}
	}

	// 5
	@GET
	@Path("top_ten_standard_deviation_by_artist")
	public Response topTenStandardDeviationByArtist() {
		Map<String, Float> topTenStandardDeviationByArtist = statisticsBO.getTopTenStandardDeviationByArtist();

		try {
			return Response.ok(MAPPER.writeValueAsString(topTenStandardDeviationByArtist)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading top ten ratings standard deviation " + topTenStandardDeviationByArtist + " as json.", e);
			return Response.serverError().build();
		}
	}

	// 6
	@GET
	@Path("top_five_popular_genres")
	public Response topFivePopularGenres() {
		Map<String, Integer> topFivePopularGenres = statisticsBO.getTopFivePopularGenres();

		try {
			return Response.ok(MAPPER.writeValueAsString(topFivePopularGenres)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading top five popular genres " + topFivePopularGenres + " as json.", e);
			return Response.serverError().build();
		}
	}

	// 7
	@GET
	@Path("top_ten_knows_with_most_shared_artists")
	public Response topTenKnownWithMostSharedArtists() {
		Map<String, Integer> topTenKnownWithMostSharedArtists = statisticsBO.getTopTenKnownWithMostSharedArtists();

		try {
			return Response.ok(MAPPER.writeValueAsString(topTenKnownWithMostSharedArtists)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading top ten known with most shared artists in common" + topTenKnownWithMostSharedArtists + " as json.", e);
			return Response.serverError().build();
		}
	}

	// 8 - ????

	// 9
	@GET
	@Path("artists_popularity")
	public Response artistsPopularity() {
		Map<Long, Integer> artistsPopularity = statisticsBO.getArtistsPopularity();

		try {
			return Response.ok(MAPPER.writeValueAsString(artistsPopularity)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading artists popularity " + artistsPopularity + " as json.", e);
			return Response.serverError().build();
		}
	}

	// 10
	@GET
	@Path("people_likes_by_amount")
	public Response peopleLikesByAmount() {
		Map<Integer, Integer> peopleLikesByAmount = statisticsBO.getPeopleLikesByAmount();

		try {
			return Response.ok(MAPPER.writeValueAsString(peopleLikesByAmount)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading people likes by amount " + peopleLikesByAmount + " as json.", e);
			return Response.serverError().build();
		}
	}

	// 11
	@GET
	@Path("artists_likes_by_amount")
	public Response artistsLikesByAmount() {
		Map<Integer, Integer> artistsLikesByAmount = statisticsBO.getArtistsLikesByAmount();

		try {
			return Response.ok(MAPPER.writeValueAsString(artistsLikesByAmount)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading artists likes by amount " + artistsLikesByAmount + " as json.", e);
			return Response.serverError().build();
		}
	}

	// 12 - ????

	// 13-a
	@GET
	@Path("top_ten_countries_with_most_artists")
	public Response topTenCountriesWithMostArtists() {
		Map<String, Integer> topTenCountriesWithMostArtists = statisticsBO.getTopTenCountriesWithMostArtists();

		try {
			return Response.ok(MAPPER.writeValueAsString(topTenCountriesWithMostArtists)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading top ten countries with most artists " + topTenCountriesWithMostArtists + " as json.", e);
			return Response.serverError().build();
		}
	}

	// 13-b
	@GET
	@Path("top_ten_eclectic_people")
	public Response topTenEclecticPeople() {
		Map<String, Integer> topTenEclecticPeople = statisticsBO.getTopTenEclecticPeople();

		try {
			return Response.ok(MAPPER.writeValueAsString(topTenEclecticPeople)).build();
		} catch (Exception e) {
			logEx.error("[STATISTICS] Error reading top ten eclectic people " + topTenEclecticPeople + " as json.", e);
			return Response.serverError().build();
		}
	}

	@GET
	@Path("all")
	public Response getAll() {
		JsonObject statistics = new JsonObject();
		statistics.add("general_ratings", new GsonBuilder().create().toJsonTree(statisticsBO.getGeneralRatings()));
		statistics.add("average_by_artist", new GsonBuilder().create().toJsonTree(statisticsBO.getAverageByArtist())); 
		statistics.add("top_twenty_averages_by_artist", new GsonBuilder().create().toJsonTree(statisticsBO.getTopTwentyAveragesByArtist()));
		statistics.add("top_ten_popular_artists", new GsonBuilder().create().toJsonTree(statisticsBO.getTopTenPopularArtists()));
		statistics.add("top_ten_standard_deviation_by_artist", new GsonBuilder().create().toJsonTree(statisticsBO.getTopTenStandardDeviationByArtist()));
		statistics.add("top_five_popular_genres", new GsonBuilder().create().toJsonTree(statisticsBO.getTopFivePopularGenres()));
		statistics.add("top_ten_knows_with_most_shared_artists", new GsonBuilder().create().toJsonTree(statisticsBO.getTopTenKnownWithMostSharedArtists()));
		statistics.add("artists_popularity", new GsonBuilder().create().toJsonTree(statisticsBO.getArtistsPopularity()));
		statistics.add("people_likes_by_amount", new GsonBuilder().create().toJsonTree(statisticsBO.getPeopleLikesByAmount()));
		statistics.add("artists_likes_by_amount", new GsonBuilder().create().toJsonTree(statisticsBO.getArtistsLikesByAmount()));
		statistics.add("top_ten_countries_with_most_artists", new GsonBuilder().create().toJsonTree(statisticsBO.getTopTenCountriesWithMostArtists())); 
		statistics.add("top_ten_eclectic_people", new GsonBuilder().create().toJsonTree(statisticsBO.getTopTenEclecticPeople())); 

		return Response.ok(statistics.toString()).build();
	}
}
