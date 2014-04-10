package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.database.datasource.DataSourceType;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MusicalArtistDAOImpl extends DAO implements MusicalArtistDAO {

    private static MusicalArtistDAOImpl instance;

    private static final String DB_ALIAS = "TCBOOK_DB";

    private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
    private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private MusicalArtistDAOImpl() {
        super();
    }

    public static MusicalArtistDAOImpl getInstance() {
        if (instance == null) {
            instance = new MusicalArtistDAOImpl();
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

	@Override
    public MusicalArtist find(final Long id) {
        MusicalArtist result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM artistamusical");
            sb.append(" WHERE id=? LIMIT 1");

            long before = System.currentTimeMillis();
            result = (MusicalArtist) getJdbc().queryForObject(sb.toString(), new Object[]{id}, new MusicalArtistRowMapper());
            log.info("[MUSICAL_ARTIST_DAO] MusicalArtist {} found in database in " + (System.currentTimeMillis() - before) + "ms", result);
        } catch (Exception e) {
            log.error("[MUSICAL_ARTIST_DAO] Error searching MUSICAL_ARTIST_DAO id: {}. Exception " + e, id);
            logEx.error("Error searching MusicalArtist", e);
        }
        return result;
    }

    @Override
    public List<MusicalArtist> findAll() {
        List<MusicalArtist> result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM artistamusical");

            long before = System.currentTimeMillis();
            List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
            result = new ArrayList<MusicalArtist>();

            for (Map<String, Object> row : rows) {
                MusicalArtist artist = new MusicalArtist();
                artist.setId(new Long((Integer) row.get("id")));
                artist.setArtisticName(row.get("nome_artistico").toString());
                artist.setCountry(row.get("pais").toString());
                artist.setGenre(row.get("genero").toString());
                artist.setUrl(row.get("url").toString());
                result.add(artist);
            }

            log.info("[MUSICAL_ARTIST_DAO] All MusicalArtist found in database in " + (System.currentTimeMillis() - before) + "ms");
        } catch (Exception e) {
            log.error("[MUSICAL_ARTIST_DAO] Error searching for all MusicalArtist. Exception " + e);
            logEx.error("Error searching for all MusicalArtist", e);
        }
        return result;
    }

    @Override
    public void insert(final MusicalArtist artist) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO artistamusical");
            sb.append(" (nome_artistico,");
            sb.append("pais,");
            sb.append("genero,");
            sb.append("url)");
            sb.append(" VALUES (?,?,?,?)");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    int i = 1;

                    ps.setString(i++, artist.getArtisticName());

                    ps.setString(i++, artist.getCountry());

                    ps.setString(i++, artist.getGenre());

                    ps.setString(i++, artist.getUrl());

                    return ps;
                }
            });
            log.info("[MUSICAL_ARTIST_DAO] MusicalArtist {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", artist);
        } catch (Exception e) {
            log.error("[MUSICAL_ARTIST_DAO] Error persisting MusicalArtist {}. Exception " + e, artist);
            logEx.error("Error persisting MusicalArtist", e);
        }
    }

    @Override
    public void remove(final Long id) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM artistamusical WHERE id = ?");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());

                    ps.setInt(1, id.intValue());

                    return ps;
                }
            });
            log.info("[MUSICAL_ARTIST_DAO] MusicalArtist id: {} removed from database in " + (System.currentTimeMillis() - before) + "ms", id);
        } catch (Exception e) {
            log.error("[MUSICAL_ARTIST_DAO] Error removing MusicalArtist id: {}. Exception " + e, id);
            logEx.error("Error removing MusicalArtist", e);
        }
    }

    public void update(final MusicalArtist artist) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("UPDATE artistamusical");
            sb.append(" SET nome_artistico=?,");
            sb.append("pais=?, ");
            sb.append("genero=?, ");
            sb.append("url=?, ");
            sb.append(" WHERE id=?");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    int i = 1;

                    ps.setString(i++, artist.getArtisticName());

                    ps.setString(i++, artist.getCountry());

                    ps.setString(i++, artist.getGenre());

                    ps.setString(i++, artist.getUrl());

                    return ps;
                }
            });
            log.info("[MUSICAL_ARTIST_DAO] MusicalArtist {} updated in " + (System.currentTimeMillis() - before) + "ms", artist);
        } catch (Exception e) {
            log.error("[MUSICAL_ARTIST_DAO] Error updating MusicalArtist {}. Exception " + e, artist);
            logEx.error("Error updating MusicalArtist", e);
        }
    }

    private class MusicalArtistRowMapper implements RowMapper<Object> {

        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {

            MusicalArtist artist = new MusicalArtist();
            artist.setId(new Long(resultSet.getInt("id")));
            artist.setArtisticName(resultSet.getString("nome_artistico"));
            artist.setCountry(resultSet.getString("pais"));
            artist.setGenre(resultSet.getString("genero"));
            artist.setUrl(resultSet.getString("url"));

            return artist;
        }
    }
}
