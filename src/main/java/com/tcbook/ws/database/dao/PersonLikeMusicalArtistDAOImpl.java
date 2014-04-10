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

public class PersonLikeMusicalArtistDAOImpl extends DAO implements PersonLikeMusicalArtistDAO {

    private static PersonLikeMusicalArtistDAOImpl instance;

    private static final String DB_ALIAS = "TCBOOK_DB";

    private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
    private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private PersonLikeMusicalArtistDAOImpl() {
        super();
    }

    public static PersonLikeMusicalArtistDAOImpl getInstance() {
        if (instance == null) {
            instance = new PersonLikeMusicalArtistDAOImpl();
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

    public void insert(final Long idPerson, final Long idMusicalArtist, final Integer rating) throws SQLException {
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

                    ps.setInt(i++, idPerson.intValue());

                    ps.setInt(i++, idMusicalArtist.intValue());

                    ps.setInt(i++, rating);

                    ps.setDate(i++, new Date(System.currentTimeMillis()));

                    return ps;
                }
            });
            log.info("[person_LIKE_MUSICAL_ARTIST] Person {} rating MusicalArtist {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", idPerson, idMusicalArtist);
        } catch (Exception e) {
            log.error("[person_LIKE_MUSICAL_ARTIST] Error persisting Person {} rating MusicalArtist {}. Exception " + e, idPerson, idMusicalArtist);
            logEx.error("Error persisting person like musical artist", e);
        }
    }

    public void remove(final Long idPerson, final Long idMusicalArtist) throws SQLException {
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

                    ps.setInt(i++, idPerson.intValue());

                    ps.setInt(i++, idMusicalArtist.intValue());
                    return ps;
                }
            });
            log.info("[person_LIKE_MUSICAL_ARTIST] Person {} rating MusicalArtist {} removed from database in " + (System.currentTimeMillis() - before) + "ms", idPerson, idMusicalArtist);
        } catch (Exception e) {
            log.error("[person_LIKE_MUSICAL_ARTIST] Error removing Person {} rating MusicalArtist {}. Exception " + e, idPerson, idMusicalArtist);
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
            log.info("[person_LIKE_MUSICAL_ARTIST] Ratings for MusicalArtist {} removed from database in " + (System.currentTimeMillis() - before) + "ms", idMusicalArtist);
        } catch (Exception e) {
            log.error("[person_LIKE_MUSICAL_ARTIST] Error removing ratings for MusicalArtist {}. Exception " + e, idMusicalArtist);
            logEx.error("Error removing ratings for MusicalArtist", e);
        }
    }

    public void removeAllForPerson(final Long idPerson) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM pessoacurteartistamusical WHERE ");
            sb.append("id_pessoa = ?");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());

                    ps.setInt(1, idPerson.intValue());

                    return ps;
                }
            });
            log.info("[person_LIKE_MUSICAL_ARTIST] Person {} ratings removed from database in " + (System.currentTimeMillis() - before) + "ms", idPerson);
        } catch (Exception e) {
            log.error("[person_LIKE_MUSICAL_ARTIST] Error removing Person {} ratings. Exception " + e, idPerson);
            logEx.error("Error removing person ratings", e);
        }
    }

}