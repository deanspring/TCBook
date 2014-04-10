package com.tcbook.ws.web.rest;

import com.google.gson.Gson;
import com.tcbook.ws.core.bo.PersonBO;
import com.tcbook.ws.database.dao.PeopleDAOImpl;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/people")
public class PeopleRS {

    private static Logger logReqAnswered = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_REQUESTS_ANSWERED);

    private static PersonBO personBO = new PersonBO();

    @GET
    @Path("/")
    public Response getAll() {
        logReqAnswered.info("list of people.");

        return Response.ok(new Gson().toJson(personBO.getAll())).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        logReqAnswered.info("get people " + id);

        return Response.ok(new Gson().toJson(personBO.getPerson(id))).build();
    }
}
