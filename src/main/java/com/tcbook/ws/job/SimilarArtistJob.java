package com.tcbook.ws.job;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.bean.SimilarMusicalArtist;
import com.tcbook.ws.core.bo.MusicalArtistBO;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by caiouvini on 5/15/14.
 */
public class SimilarArtistJob {

    private static final Logger log = LoggerFactory.getLogger(DataExtractionJob.class);
    private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private static MusicalArtistBO musicalArtistBO = new MusicalArtistBO();

    public static void extractSimilarArtists() {
        long before = System.currentTimeMillis();

        try {
            log.info("[SIMILAR_ARTIST_EXTRACTION_JOB] Started SimilarArtistJob on {}.", new Date());

            EchoNestAPI echonest = new EchoNestAPI(TCBookProperties.getInstance().getString("echonest.api_key"));

            List<MusicalArtist> musicalArtists = musicalArtistBO.getAll();
            for (MusicalArtist artist : musicalArtists) {

                boolean extractedArtists = false;

                while (!extractedArtists) {
                    try {
                        List<Artist> echonestArtists = echonest.searchArtists(artist.getArtisticName());
                        extractedArtists = Boolean.TRUE;

                        if (echonestArtists != null && !echonestArtists.isEmpty()) {
                            Artist echonestArtist = echonestArtists.get(0);

                            boolean extractedSimilar = false;

                            while (!extractedSimilar) {
                                try {
                                    List<Artist> similarArtists = echonestArtist.getSimilar(5);
                                    extractedSimilar = Boolean.TRUE;

                                    if (similarArtists != null && !similarArtists.isEmpty()) {
                                        for (Artist similarArtist : similarArtists) {

                                            String similarName = similarArtist.getName();
                                            String similarMbid = similarArtist.getForeignID("musicbrainz");
                                            if (StringUtils.isNotBlank(similarMbid)) {
                                                similarMbid = similarMbid.replace("musicbrainz:artist:", "");
                                            }

                                            SimilarMusicalArtist similarMusicalArtist = new SimilarMusicalArtist();
                                            similarMusicalArtist.setMbid(similarMbid);
                                            similarMusicalArtist.setArtisticName(similarName);

                                            musicalArtistBO.setSimilarArtist(similarMusicalArtist, artist);
                                        }
                                    }
                                } catch (EchoNestException e) {
                                    log.error("Echonest limit exceeded. will retry.");
                                    try {
                                        Thread.sleep(10000);
                                    } catch (Exception e1) {
                                        // do nothing
                                    }
                                }
                            }

                        }
                    } catch (EchoNestException e) {
                        log.error("Echonest limit exceeded. will retry.");
                        try {
                            Thread.sleep(10000);
                        } catch (Exception e1) {
                            // do nothing
                        }
                    }
                }

            }

        } catch (Exception e) {
            log.error("[SIMILAR_ARTIST_EXTRACTION_JOB] Error performing job. Exception: " + e);
            logEx.error("Error performing job.", e);
        } finally {
            log.info("[SIMILAR_ARTIST_EXTRACTION_JOB] Finished SimilarArtistJob in {}ms.", (System.currentTimeMillis() - before));
        }
    }

}
