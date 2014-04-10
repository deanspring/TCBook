package com.tcbook.ws.web.rest;

import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/general")
public class GeneralRequestHandler {

    private static Logger logReqAnswered = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_REQUESTS_ANSWERED);

    public GeneralRequestHandler() {
    }

    @GET
    @Path("ping")
    public String handlePing() {
        logReqAnswered.info("Answered ping.");
        return "OK";
    }

}
