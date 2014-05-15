package com.tcbook.ws.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.bean.MusicalGenre;
import com.tcbook.ws.util.TCBookConstants;

public class MusicalArtistDAOImpl extends DAO implements MusicalArtistDAO {

	private static MusicalArtistDAOImpl instance;

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
	public MusicalArtist find(final Long id) {
		MusicalArtist result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM ArtistaMusical");
			sb.append(" WHERE id=? LIMIT 1");

			long before = System.currentTimeMillis();
			result = (MusicalArtist) getJdbc().queryForObject(sb.toString(), new Object[] { id }, new MusicalArtistRowMapper());
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
			sb.append("SELECT * FROM ArtistaMusical");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
			result = new ArrayList<MusicalArtist>();

			for (Map<String, Object> row : rows) {
				MusicalArtist artist = new MusicalArtist();
				artist.setId(new Long((Integer) row.get("id")));
				artist.setArtisticName(row.get("nome_artistico").toString());
				artist.setIdRegion(row.get("id_regiao") != null ? new Long((Integer) row.get("id_regiao")) : null);
				artist.setUrl(row.get("url").toString());
				artist.setMbid(row.get("mbid") != null ? row.get("mbid").toString() : null);
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
			sb.append("INSERT INTO ArtistaMusical");
			sb.append(" (nome_artistico,");
			sb.append("id_regiao,");
			sb.append("url,");
			sb.append("mbid)");
			sb.append(" VALUES (?,?,?,?)");

			long before = System.currentTimeMillis();
			getJdbc().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());
					int i = 1;

					ps.setString(i++, artist.getArtisticName());

					ps.setLong(i++, artist.getIdRegion());

					ps.setString(i++, artist.getUrl());

					ps.setString(i++, artist.getMbid());

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
			sb.append("DELETE FROM ArtistaMusical WHERE id = ?");

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
			sb.append("UPDATE ArtistaMusical");
			sb.append(" SET nome_artistico=?,");
			sb.append("id_regiao=?, ");
			sb.append("url=?, ");
			sb.append("mbid=? ");
			sb.append(" WHERE id=?");

			long before = System.currentTimeMillis();
			getJdbc().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());
					int i = 1;

					ps.setString(i++, artist.getArtisticName());

					if (artist.getIdRegion() != null)
						ps.setLong(i++, artist.getIdRegion());
					else
						ps.setNull(i++, Types.INTEGER);

					ps.setString(i++, artist.getUrl());

					ps.setString(i++, artist.getMbid());

					ps.setInt(i++, artist.getId().intValue());

					return ps;
				}
			});
			log.info("[MUSICAL_ARTIST_DAO] MusicalArtist {} updated in " + (System.currentTimeMillis() - before) + "ms", artist);
		} catch (Exception e) {
			e.printStackTrace();
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
			artist.setIdRegion(((Long) resultSet.getLong("id_regiao") != null) ? (Long) resultSet.getLong("id_regiao") : null);
			artist.setUrl(resultSet.getString("url"));
			artist.setMbid(resultSet.getString("mbid"));

			return artist;
		}
	}

	@Override
	public List<MusicalGenre> getGenresOfArtist(Long artistId) {
		List<MusicalGenre> results = new ArrayList<MusicalGenre>();
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM GeneroMusical WHERE id IN (SELECT id_genero FROM GeneroArtistaMusical WHERE id_artista_musical = ?)");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), artistId);

			if (rows != null && rows.size() > 0) {
				for (Map<String, Object> row : rows) {
					MusicalGenre musicalGenre = new MusicalGenre();

					musicalGenre.setId(new Long((Integer) row.get("id")));
					musicalGenre.setName(new String(row.get("nome_genero").toString()));
					results.add(musicalGenre);
				}
			}

			log.info("[CITY] Genre for id {} found in database in " + (System.currentTimeMillis() - before) + "ms", artistId);
		} catch (Exception e) {
			log.error("[CITY] Error searching genre of artist {}. Exception " + e, artistId);
			logEx.error("Error searching genre", e);
		}

		return results;
	}
}
