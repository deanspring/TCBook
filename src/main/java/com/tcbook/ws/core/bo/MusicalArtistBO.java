package com.tcbook.ws.core.bo;

import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.bean.MusicalGenre;
import com.tcbook.ws.database.dao.MusicalArtistDAO;
import com.tcbook.ws.database.dao.MusicalArtistDAOImpl;
import com.tcbook.ws.database.dao.MusicalArtistGenresDAO;
import com.tcbook.ws.database.dao.MusicalArtistGenresDAOImpl;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by caiouvini on 5/11/14.
 */
public class MusicalArtistBO {

    private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private MusicalArtistDAO musicalArtistDAO = MusicalArtistDAOImpl.getInstance();
    private MusicalArtistGenresDAO musicalArtistGenresDAO = MusicalArtistGenresDAOImpl.getInstance();

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

}
