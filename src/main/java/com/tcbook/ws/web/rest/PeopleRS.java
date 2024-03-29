package com.tcbook.ws.web.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.bean.Person;
import com.tcbook.ws.core.bo.PersonBO;
import com.tcbook.ws.database.dao.MusicalArtistDAO;
import com.tcbook.ws.database.dao.MusicalArtistDAOImpl;
import com.tcbook.ws.util.TCBookConstants;

@Path("/people")
public class PeopleRS {

    private static Logger logReqAnswered = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_REQUESTS_ANSWERED);

    private static PersonBO personBO = new PersonBO();
    private MusicalArtistDAO musicalArtistDAO = MusicalArtistDAOImpl.getInstance();

    @POST
    @Path("/")
    public Response newPerson(@FormParam("person") String personJson) {
        logReqAnswered.info("save new person.");
        personBO.save(new Gson().fromJson(personJson, Person.class));

        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    public Response editPerson(@PathParam("id") Long id, @FormParam("person") String personJson) {
        logReqAnswered.info("edit person " + id + ".");
        personBO.save(new Gson().fromJson(personJson, Person.class));

        return Response.ok().build();
    }

    @GET
    @Path("/")
    public Response getAll() {
        logReqAnswered.info("list of people.");

        return Response.ok(new Gson().toJson(personBO.getAll())).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        logReqAnswered.info("get person " + id);

        return Response.ok(new Gson().toJson(personBO.getPerson(id))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeById(@PathParam("id") Long id) {
        logReqAnswered.info("delete person " + id);

        personBO.disable(id);

        return Response.ok().build();
    }

    @GET
    @Path("/{id}/social")
    public Response getSocialById(@PathParam("id") Long id) {
        logReqAnswered.info("get knows of the person " + id);

        return Response.ok(new Gson().toJson(personBO.getColleaguesForPersonWithId(id))).build();
    }

    @PUT
    @Path("/{id}/social")
    public Response setSocialById(@PathParam("id") Long id, @FormParam("social") String colleaguesIds) {
        logReqAnswered.info("set knows of the person " + id);

        personBO.setColleaguesForPersonWithId(id, new Gson().<List<Long>>fromJson(colleaguesIds, new TypeToken<List<Long>>() {
        }.getType()));

        return Response.ok().build();
    }

    @GET
    @Path("/{id}/musicalArtists")
    public Response getMusicalArtistById(@PathParam("id") Long id) {
        logReqAnswered.info("get musicals artists of the person " + id);

        return Response.ok(new Gson().toJson(personBO.getMusicalsArtistsForPersonWithId(id))).build();
    }

    @PUT
    @Path("/{id}/musicalArtist")
    public Response setMusicalArtistById(@PathParam("id") Long id, @FormParam("artists") String artistsIds) {
        logReqAnswered.info("set musicals Artists of the person " + id);

        personBO.setLikes(id, new Gson().<Map<Long, Integer>>fromJson(artistsIds, new TypeToken<Map<Long, Integer>>() {
        }.getType()));

        return Response.ok().build();
    }

    @GET
    @Path("/{id}/recommend")
    public Response recommendArtistsByPersonId(@PathParam("id") Long personId) {

        long before = System.currentTimeMillis();

        List<MusicalArtist> recommendedArtists = personBO.recommendArtistsForPerson(personId);

        String artistsJson = musicalArtistDAO.toJson(recommendedArtists);

        logReqAnswered.info("recommended artists for person {}: {}. Time it took " + (System.currentTimeMillis() - before) + "ms", personId, recommendedArtists);

        return Response.ok(artistsJson).build();

    }
}
