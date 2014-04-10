package com.tcbook.ws.web.rest;

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
import com.tcbook.ws.bean.Person;
import com.tcbook.ws.core.bo.PersonBO;
import com.tcbook.ws.util.TCBookConstants;

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

}
