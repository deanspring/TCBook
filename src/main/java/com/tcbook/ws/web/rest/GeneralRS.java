package com.tcbook.ws.web.rest;

import com.tcbook.ws.job.DataCleaningJob;
import com.tcbook.ws.job.DataExtractionJob;
import com.tcbook.ws.job.GenresJob;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/general")
public class GeneralRS {

	private static Logger logReqAnswered = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_REQUESTS_ANSWERED);

	public GeneralRS() {
	}

	@GET
	@Path("ping")
	public String handlePing() {
		logReqAnswered.info("Answered ping.");
		return "OK";
	}

	@GET
	@Path("data_cleaning")
	public Response cleanData() {
		DataCleaningJob.cleanData();
		return Response.ok().build();
	}

	@GET
	@Path("data_extraction")
	public Response extractData() {
		DataExtractionJob.extractData();
		return Response.ok().build();
	}

	@GET
	@Path("update_genres")
	public Response updateGenres() {
		GenresJob.updateGenres();
		return Response.ok().build();
	}

}
