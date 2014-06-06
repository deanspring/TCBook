package com.tcbook.ws.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcbook.ws.database.dao.MusicalArtistDAO;
import com.tcbook.ws.database.dao.MusicalArtistDAOImpl;
import com.tcbook.ws.util.TCBookConstants;

@Path("/musicalArtist")
public class MusicalArtistRS {

	private static Logger logReqAnswered = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_REQUESTS_ANSWERED);

	private MusicalArtistDAO musicalArtistDAO = MusicalArtistDAOImpl.getInstance();

	@GET
	@Path("/")
	public Response getAll() {
		logReqAnswered.info("list of musicals artists.");

		return Response.ok(musicalArtistDAO.findAllAsJson()).build();
	}

}
