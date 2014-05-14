package com.tcbook.ws.web.rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcbook.ws.bean.Person;
import com.tcbook.ws.core.bo.PersonBO;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/people")
public class PeopleRS {

	private static Logger logReqAnswered = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_REQUESTS_ANSWERED);

	private static PersonBO personBO = new PersonBO();

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

		personBO.setColleaguesForPersonWithId(id, new Gson().<List<Long>> fromJson(colleaguesIds, new TypeToken<List<Long>>() {
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

		personBO.setLikes(id, new Gson().<Map<Long, Integer>> fromJson(artistsIds, new TypeToken<Map<Long, Integer>>() {
		}.getType()));

		return Response.ok().build();
	}
}
