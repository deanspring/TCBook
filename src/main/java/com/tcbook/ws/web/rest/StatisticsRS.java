package com.tcbook.ws.web.rest;

import com.tcbook.ws.core.bo.StatisticsBO;
import com.tcbook.ws.util.TCBookConstants;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by caiouvini on 5/20/14.
 */
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
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(generalRatings);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading general ratings " + generalRatings + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }

    // 2
    @GET
    @Path("average_by_artist")
    public Response averageByArtist() {
        Map<String, Float> averageByArtist = statisticsBO.getAverageByArtist();
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(averageByArtist);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading average ratings by artist" + averageByArtist + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }

    // 3
    @GET
    @Path("top_twenty_averages_by_artist")
    public Response topTwentyAveragesByArtist() {
        Map<String, Float> topTwentyAveragesByArtist = statisticsBO.getTopTwentyAveragesByArtist();
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(topTwentyAveragesByArtist);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading top twenty average ratings by artist " + topTwentyAveragesByArtist + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }

    // 4
    @GET
    @Path("top_ten_popular_artists")
    public Response topTenPopularArtists() {
        Map<String, Integer> topTenPopularArtists = statisticsBO.getTopTenPopularArtists();
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(topTenPopularArtists);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading top ten popular artists " + topTenPopularArtists + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }

    // 5
    @GET
    @Path("top_ten_standard_deviation_by_artist")
    public Response topTenStandardDeviationByArtist() {
        Map<String, Float> topTenStandardDeviationByArtist = statisticsBO.getTopTenStandardDeviationByArtist();
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(topTenStandardDeviationByArtist);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading top ten ratings standard deviation " + topTenStandardDeviationByArtist + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }

    // 6
    @GET
    @Path("top_five_popular_genres")
    public Response topFivePopularGenres() {
        Map<String, Integer> topFivePopularGenres = statisticsBO.getTopFivePopularGenres();
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(topFivePopularGenres);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading top five popular genres " + topFivePopularGenres + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }

    // 7
    @GET
    @Path("top_ten_kowns_with_most_shared_artists")
    public Response topTenKnownWithMostSharedArtists() {
        Map<String, Integer> topTenKnownWithMostSharedArtists = statisticsBO.getTopTenKnownWithMostSharedArtists();
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(topTenKnownWithMostSharedArtists);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading top ten known with most shared artists in common" + topTenKnownWithMostSharedArtists + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }

    // 8 - ????

    // 9
    @GET
    @Path("artists_popularity")
    public Response artistsPopularity() {
        Map<Long, Integer> artistsPopularity = statisticsBO.getArtistsPopularity();
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(artistsPopularity);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading artists popularity " + artistsPopularity + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }

    // 10
    @GET
    @Path("people_likes_by_amount")
    public Response peopleLikesByAmount() {
        Map<Integer, Integer> peopleLikesByAmount = statisticsBO.getPeopleLikesByAmount();
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(peopleLikesByAmount);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading people likes by amount " + peopleLikesByAmount + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }

    // 11
    @GET
    @Path("artists_likes_by_amount")
    public Response artistsLikesByAmount() {
        Map<Integer, Integer> artistsLikesByAmount = statisticsBO.getArtistsLikesByAmount();
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(artistsLikesByAmount);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading artists likes by amount " + artistsLikesByAmount + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }

    // 12 - ????

    // 13-a
    @GET
    @Path("top_ten_countries_with_most_artists")
    public Response topTenCountriesWithMostArtists() {
        Map<String, Integer> topTenCountriesWithMostArtists = statisticsBO.getTopTenCountriesWithMostArtists();
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(topTenCountriesWithMostArtists);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading top ten countries with most artists " + topTenCountriesWithMostArtists + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }

    // 13-b
    @GET
    @Path("top_ten_eclectic_people")
    public Response topTenEclecticPeople() {
        Map<String, Integer> topTenEclecticPeople = statisticsBO.getTopTenEclecticPeople();
        String responseJson;

        try {
            responseJson = MAPPER.writeValueAsString(topTenEclecticPeople);
        } catch (Exception e) {
            logEx.error("[STATISTICS] Error reading top ten eclectic people " + topTenEclecticPeople + " as json.", e);
            return Response.serverError().build();
        }

        return Response.ok(responseJson).build();
    }


}
