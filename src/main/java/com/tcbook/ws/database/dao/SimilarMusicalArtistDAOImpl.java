package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.SimilarMusicalArtist;
import com.tcbook.ws.util.TCBookConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * Created by caiouvini on 5/15/14.
 */
public class SimilarMusicalArtistDAOImpl extends DAO implements SimilarMusicalArtistDAO {

    private static SimilarMusicalArtistDAOImpl instance;

    private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
    private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private SimilarMusicalArtistDAOImpl() {
        super();
    }

    public static SimilarMusicalArtistDAOImpl getInstance() {
        if (instance == null) {
            instance = new SimilarMusicalArtistDAOImpl();
        }
        return instance;
    }

    @Override
    public void insert(final SimilarMusicalArtist similarMusicalArtist) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ArtistaMusicalSemelhante");
            sb.append(" (nome_artistico,");
            sb.append(" mbid)");
            sb.append(" VALUES (?,?)");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    int i = 1;

                    ps.setString(i++, similarMusicalArtist.getArtisticName());

                    if (StringUtils.isNotBlank(similarMusicalArtist.getMbid())) {
                        ps.setString(i++, similarMusicalArtist.getMbid());
                    } else {
                        ps.setNull(i++, Types.VARCHAR);
                    }

                    return ps;
                }
            });
            log.info("[SIMILAR_MUSICAL_ARTIST_DAO] SimilarMusicalArtist {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", similarMusicalArtist);
        } catch (Exception e) {
            log.error("[SIMILAR_MUSICAL_ARTIST_DAO] Error persisting SimilarMusicalArtist {}. Exception " + e, similarMusicalArtist);
            logEx.error("Error persisting SimilarMusicalArtist", e);
        }
    }

    @Override
    public SimilarMusicalArtist getByName(String name) {
        SimilarMusicalArtist result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ArtistaMusicalSemelhante WHERE nome_artistico = ?");

            long before = System.currentTimeMillis();
            List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), name);

            if (rows != null && rows.size() > 0) {
                result = new SimilarMusicalArtist();
                Map<String, Object> row = rows.get(0);

                result.setId(new Long((Integer) row.get("id")));
                result.setArtisticName((String) row.get("nome_artistico"));
                result.setMbid((String) row.get("mbid"));
            }

            log.info("[SIMILAR_MUSICAL_ARTIST_DAO] SimilarMusicalArtist for name {} found in database in " + (System.currentTimeMillis() - before) + "ms", name);
        } catch (Exception e) {
            log.error("[SIMILAR_MUSICAL_ARTIST_DAO] Error searching similar musical artist for name {}. Exception " + e, name);
            logEx.error("Error searching similar musical artist", e);
        }

        return result;
    }

    public SimilarMusicalArtist getByMbid(String mbid) {
        SimilarMusicalArtist result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ArtistaMusicalSemelhante WHERE mbid = ?");

            long before = System.currentTimeMillis();
            List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), mbid);

            if (rows != null && rows.size() > 0) {
                result = new SimilarMusicalArtist();
                Map<String, Object> row = rows.get(0);

                result.setId(new Long((Integer) row.get("id")));
                result.setArtisticName((String) row.get("nome_artistico"));
                result.setMbid((String) row.get("mbid"));
            }

            log.info("[SIMILAR_MUSICAL_ARTIST_DAO] SimilarMusicalArtist for mbid {} found in database in " + (System.currentTimeMillis() - before) + "ms", mbid);
        } catch (Exception e) {
            log.error("[SIMILAR_MUSICAL_ARTIST_DAO] Error searching similar musical artist for mbid {}. Exception " + e, mbid);
            logEx.error("Error searching similar musical artist", e);
        }

        return result;
    }

}
