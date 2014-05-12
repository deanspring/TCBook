package com.tcbook.ws.job;

import com.tcbook.ws.database.dao.MusicalArtistDAO;
import com.tcbook.ws.database.dao.MusicalArtistDAOImpl;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by caiouvini on 5/11/14.
 */
public class DataCleaningJob {

    private static final Logger log = LoggerFactory.getLogger(DataCleaningJob.class);
    private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private static MusicalArtistDAO musicalArtistDAO = MusicalArtistDAOImpl.getInstance();

    public static void cleanData() {
        long before = System.currentTimeMillis();
        try {
            log.info("[TCBOOK] Started DataCleaningJob on {}.", new Date());

        } catch (Exception e) {

        } finally {
            log.info("[TCBOOK] Finished DataCleaningJob in {}ms.", (System.currentTimeMillis() - before));
        }
    }

}
