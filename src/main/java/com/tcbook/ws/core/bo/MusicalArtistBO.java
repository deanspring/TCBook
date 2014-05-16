package com.tcbook.ws.core.bo;

import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.bean.MusicalArtistSimilarity;
import com.tcbook.ws.bean.MusicalGenre;
import com.tcbook.ws.bean.SimilarMusicalArtist;
import com.tcbook.ws.database.dao.*;
import com.tcbook.ws.util.TCBookConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class MusicalArtistBO {

    private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private MusicalArtistDAO musicalArtistDAO = MusicalArtistDAOImpl.getInstance();
    private MusicalArtistGenresDAO musicalArtistGenresDAO = MusicalArtistGenresDAOImpl.getInstance();
    private SimilarMusicalArtistDAO similarMusicalArtistDAO = SimilarMusicalArtistDAOImpl.getInstance();
    private MusicalArtistSimilarityDAO musicalArtistSimilarityDAO = MusicalArtistSimilarityDAOImpl.getInstance();

    public List<MusicalArtist> getAll() {
        return musicalArtistDAO.findAll();
    }

    public void update(MusicalArtist musicalArtist) throws Exception {
        try {
            musicalArtistDAO.update(musicalArtist);
        } catch (Exception e) {
            throw new Exception("Error updating musical artist. " + e.getMessage());
        }
    }

    public void setGenresForArtist(MusicalArtist artist, List<MusicalGenre> genres) {
        for (MusicalGenre genre : genres) {
            try {
                musicalArtistGenresDAO.insert(artist.getId(), genre.getId());
            } catch (Exception e) {
                logEx.error("Error inserting genre {} for artist {}", genre.getName(), artist.getId());
            }
        }
    }

    public void setSimilarArtist(SimilarMusicalArtist similarMusicalArtist, MusicalArtist musicalArtist) {

        // try to find by name
        SimilarMusicalArtist similarMusicalArtistFromBD = similarMusicalArtistDAO.getByName(similarMusicalArtist.getArtisticName());
        if (similarMusicalArtistFromBD == null && StringUtils.isNotBlank(similarMusicalArtist.getMbid())) {

            // if not found, try to find by mbid
            similarMusicalArtistFromBD = similarMusicalArtistDAO.getByMbid(similarMusicalArtist.getMbid());
        }

        if (similarMusicalArtistFromBD == null) {
            try {
                // create a new similar artist
                similarMusicalArtistDAO.insert(similarMusicalArtist);
                similarMusicalArtist = similarMusicalArtistDAO.getByName(similarMusicalArtist.getArtisticName());
            } catch (SQLException e) {
                logEx.error("Error searching similar musical artist.", e);
            }
        } else {
            // use's the previous database request
            similarMusicalArtist = similarMusicalArtistFromBD;
        }

        // create the similarity relation
        MusicalArtistSimilarity musicalArtistSimilarity = new MusicalArtistSimilarity();
        musicalArtistSimilarity.setMusicalArtistId(musicalArtist.getId());
        musicalArtistSimilarity.setSimilarMusicalArtistId(similarMusicalArtist.getId());

        try {
            musicalArtistSimilarityDAO.insert(musicalArtistSimilarity);
        } catch (SQLException e) {
            logEx.error("Error inserting musical artist similarity.", e);
        }
    }

}
