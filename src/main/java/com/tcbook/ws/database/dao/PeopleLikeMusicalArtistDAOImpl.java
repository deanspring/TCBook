package com.tcbook.ws.database.dao;

import com.tcbook.ws.database.datasource.DataSourceType;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PeopleLikeMusicalArtistDAOImpl extends DAO implements PeopleLikeMusicalArtistDAO {

    private static PeopleLikeMusicalArtistDAOImpl instance;

    private static final String DB_ALIAS = "TCBOOK_DB";

    private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
    private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private PeopleLikeMusicalArtistDAOImpl() {
        super();
    }

    public static PeopleLikeMusicalArtistDAOImpl getInstance() {
        if (instance == null) {
            instance = new PeopleLikeMusicalArtistDAOImpl();
        }
        return instance;
    }

    @Override
    protected String getDatabaseAlias() {
        return DB_ALIAS;
    }

    @Override
    protected DataSourceType getDataSourceType() {
        return DataSourceType.valueOf(TCBookProperties.getInstance().getString("tcbook.db.type"));
    }

    public void insert(final Long idPeople, final Long idMusicalArtist, final Integer rating) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO pessoacurteartistamusical");
            sb.append(" (id_pessoa,");
            sb.append(" id_artista_musical,");
            sb.append(" nota,");
            sb.append(" data_curtida)");
            sb.append(" VALUES (?,?,?,?)");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    int i = 1;

                    ps.setInt(i++, idPeople.intValue());

                    ps.setInt(i++, idMusicalArtist.intValue());

                    ps.setInt(i++, rating);

                    ps.setDate(i++, new Date(System.currentTimeMillis()));

                    return ps;
                }
            });
            log.info("[PEOPLE_LIKE_MUSICAL_ARTIST] People {} rating MusicalArtist {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", idPeople, idMusicalArtist);
        } catch (Exception e) {
            log.error("[PEOPLE_LIKE_MUSICAL_ARTIST] Error persisting People {} rating MusicalArtist {}. Exception " + e, idPeople, idMusicalArtist);
            logEx.error("Error persisting people like musical artist", e);
        }
    }

    public void remove(final Long idPeople, final Long idMusicalArtist) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM pessoacurteartistamusical WHERE ");
            sb.append("id_pessoa = ? AND ");
            sb.append("id_artista_musical = ?");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    int i = 1;

                    ps.setInt(i++, idPeople.intValue());

                    ps.setInt(i++, idMusicalArtist.intValue());
                    return ps;
                }
            });
            log.info("[PEOPLE_LIKE_MUSICAL_ARTIST] People {} rating MusicalArtist {} removed from database in " + (System.currentTimeMillis() - before) + "ms", idPeople, idMusicalArtist);
        } catch (Exception e) {
            log.error("[PEOPLE_LIKE_MUSICAL_ARTIST] Error removing People {} rating MusicalArtist {}. Exception " + e, idPeople, idMusicalArtist);
            logEx.error("Error removing colleague", e);
        }
    }

    public void removeAllForArtist(final Long idMusicalArtist) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM pessoacurteartistamusical WHERE ");
            sb.append("id_artista_musical = ?");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());

                    ps.setInt(1, idMusicalArtist.intValue());

                    return ps;
                }
            });
            log.info("[PEOPLE_LIKE_MUSICAL_ARTIST] Ratings for MusicalArtist {} removed from database in " + (System.currentTimeMillis() - before) + "ms", idMusicalArtist);
        } catch (Exception e) {
            log.error("[PEOPLE_LIKE_MUSICAL_ARTIST] Error removing ratings for MusicalArtist {}. Exception " + e, idMusicalArtist);
            logEx.error("Error removing ratings for MusicalArtist", e);
        }
    }

    public void removeAllForPeople(final Long idPeople) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM pessoacurteartistamusical WHERE ");
            sb.append("id_pessoa = ?");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());

                    ps.setInt(1, idPeople.intValue());

                    return ps;
                }
            });
            log.info("[PEOPLE_LIKE_MUSICAL_ARTIST] People {} ratings removed from database in " + (System.currentTimeMillis() - before) + "ms", idPeople);
        } catch (Exception e) {
            log.error("[PEOPLE_LIKE_MUSICAL_ARTIST] Error removing People {} ratings. Exception " + e, idPeople);
            logEx.error("Error removing people ratings", e);
        }
    }

}
