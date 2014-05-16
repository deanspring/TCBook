package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.MusicalArtistSimilarity;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by caiouvini on 5/15/14.
 */
public class MusicalArtistSimilarityDAOImpl extends DAO implements MusicalArtistSimilarityDAO {

    private static MusicalArtistSimilarityDAOImpl instance;

    private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
    private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private MusicalArtistSimilarityDAOImpl() {
        super();
    }

    public static MusicalArtistSimilarityDAOImpl getInstance() {
        if (instance == null) {
            instance = new MusicalArtistSimilarityDAOImpl();
        }
        return instance;
    }

    @Override
    public void insert(final MusicalArtistSimilarity musicalArtistSimilarity) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO SemelhancaArtistaMusical");
            sb.append(" (id_artista,");
            sb.append(" id_artista_similar)");
            sb.append(" VALUES (?,?)");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    int i = 1;

                    ps.setInt(i++, musicalArtistSimilarity.getMusicalArtistId().intValue());
                    ps.setInt(i++, musicalArtistSimilarity.getSimilarMusicalArtistId().intValue());

                    return ps;
                }
            });
            log.info("[MUSICAL_ARTIST_SIMILARITY_DAO] MusicalArtistSimilarity {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", musicalArtistSimilarity);
        } catch (Exception e) {
            log.error("[MUSICAL_ARTIST_SIMILARITY_DAO] Error persisting MusicalArtistSimilarity {}. Exception " + e, musicalArtistSimilarity);
            logEx.error("Error persisting MusicalArtistSimilarity", e);
        }
    }
}
