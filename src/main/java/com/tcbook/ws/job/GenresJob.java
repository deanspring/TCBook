package com.tcbook.ws.job;

import com.echonest.api.v4.EchoNestAPI;
import com.tcbook.ws.core.bo.MusicalGenreBO;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GenresJob {

	private static final Logger log = LoggerFactory.getLogger(GenresJob.class);
	private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

	private static MusicalGenreBO genresBO = new MusicalGenreBO();

	public static void updateGenres() {
		long before = System.currentTimeMillis();
		try {
			EchoNestAPI echonest = new EchoNestAPI(TCBookProperties.getInstance().getString("echonest.api_key"));
			List<String> listGenres = echonest.listGenres();

			for (String genreName : listGenres) {
				if (genresBO.findForName(genreName.toLowerCase()) == null) {
					genresBO.insert(genreName.toLowerCase());
					log.info("[UPDATE_GENRES] Genre {} inserted!", genreName.toLowerCase());
				} else {
					log.info("[UPDATE_GENRES] Genre {} already exists!", genreName.toLowerCase());
				}
			}

		} catch (Exception e) {
			log.error("[UPDATE_GENRES] Error updating genres. Exception: " + e);
			logEx.error("Error updating genres.", e);
		} finally {
			log.info("[UPDATE_GENRES] Update genres job finished in {}ms", (System.currentTimeMillis() - before));
		}
	}

}
