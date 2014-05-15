package com.tcbook.ws.job;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.database.dao.MusicalArtistDAO;
import com.tcbook.ws.database.dao.MusicalArtistDAOImpl;
import com.tcbook.ws.database.dao.PersonLikeMusicalArtistDAO;
import com.tcbook.ws.database.dao.PersonLikeMusicalArtistDAOImpl;
import com.tcbook.ws.util.TCBookConstants;

public class DataCleaningJob {

	private static final Logger log = LoggerFactory.getLogger(DataCleaningJob.class);
	private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

	private static MusicalArtistDAO musicalArtistDAO = MusicalArtistDAOImpl.getInstance();
	private static PersonLikeMusicalArtistDAO personLikeMusicalArtistDAO = PersonLikeMusicalArtistDAOImpl.getInstance();

	public static void cleanData() {
		long before = System.currentTimeMillis();
		try {
			log.info("[TCBOOK] Started DataCleaningJob on {}.", new Date());
			
			personLikeMusicalArtistDAO.dataCleaning();

			removeDuplicateArtists();
		} catch (Exception e) {
			e.printStackTrace();
			logEx.error(e.getMessage(), e);
		} finally {
			log.info("[TCBOOK] Finished DataCleaningJob in {}ms.", (System.currentTimeMillis() - before));
		}
	}

	protected static void removeDuplicateArtists() throws SQLException {
		Set<String> mbids = new HashSet<String>();
		List<MusicalArtist> artists = musicalArtistDAO.findAll();

		for (int i = 0; i < artists.size(); i++)
		    if (!mbids.add(artists.get(i).getMbid())) {
				musicalArtistDAO.remove(artists.get(i).getId());
		        artists.remove(i--);
			}
	}
}