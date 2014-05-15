package com.tcbook.ws.database.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.tcbook.ws.bean.MusicalArtist;
import com.tcbook.ws.util.TCBookConstants;

public class PersonLikeMusicalArtistDAOImpl extends DAO implements PersonLikeMusicalArtistDAO {

	private static PersonLikeMusicalArtistDAOImpl instance;

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

	public void insert(final Long idPerson, final Long idMusicalArtist, final Integer rating) throws SQLException {
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO PessoaCurteArtistaMusical");
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
			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Person {} rating MusicalArtist {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", idPerson, idMusicalArtist);
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error persisting Person {} rating MusicalArtist {}. Exception " + e, idPerson, idMusicalArtist);
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
			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Person {} rating MusicalArtist {} removed from database in " + (System.currentTimeMillis() - before) + "ms", idPerson, idMusicalArtist);
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error removing Person {} rating MusicalArtist {}. Exception " + e, idPerson, idMusicalArtist);
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
			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Ratings for MusicalArtist {} removed from database in " + (System.currentTimeMillis() - before) + "ms", idMusicalArtist);
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error removing ratings for MusicalArtist {}. Exception " + e, idMusicalArtist);
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
			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Person {} ratings removed from database in " + (System.currentTimeMillis() - before) + "ms", idPerson);
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error removing Person {} ratings. Exception " + e, idPerson);
			logEx.error("Error removing person ratings", e);
		}
	}

	@Override
	public Map<Long, Integer> getAllForPerson(Long idPerson) throws SQLException {
		Map<Long, Integer> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM PessoaCurteArtistaMusical WHERE id_pessoa = ?");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), idPerson);
			result = new HashMap<Long, Integer>();

			for (Map<String, Object> row : rows) {
				result.put(new Long((Integer) row.get("id_artista_musical")), Integer.valueOf(row.get("nota").toString()));
			}

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] All Likes for Person {} found in database in " + (System.currentTimeMillis() - before) + "ms", idPerson);
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for all likes for Person {}. Exception " + e, idPerson);
			logEx.error("Error searching for all likes", e);
		}

		return result;
	}

	@Override
	public void updateLike(final Long idPerson, final Long idMusicalArtist, final Integer rate) throws SQLException {
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("UPDATE PessoaCurteArtistaMusical SET nota = ? WHERE");
			sb.append(" id_pessoa = ? AND id_artista_musical = ?");

			long before = System.currentTimeMillis();
			getJdbc().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());
					int i = 1;

					ps.setInt(i++, rate);

					ps.setInt(i++, idPerson.intValue());

					ps.setInt(i++, idMusicalArtist.intValue());

					return ps;
				}
			});
			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Person {} MusicalArtist {} ratings updated in " + (System.currentTimeMillis() - before) + "ms", idPerson, idMusicalArtist);
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error updating Person {} MusicalArtist {} ratings. Exception " + e, idPerson);
			logEx.error("Error updating person ratings", e);
		}
	}

	@Override
	public void dataCleaning() {
		try {
			long before = System.currentTimeMillis();
			getJdbc().batchUpdate(
					new String[] { "ALTER TABLE Pessoa CurteArtistaMusical ADD COLUMN mbid VARCHAR(255);",
							"UPDATE PessoaCurteArtistaMusical p SET p.mbid = (SELECT mbid FROM ArtistaMusical a WHERE a.id = p.id_artsta);",
							"UPDATE PessoaCurteArtistaMusical p SET p.id_artista = (SELECT id FROM ArtistaMusical a WHERE a.mbid = p.mbid LIMIT 1);" });

			log.info("[MUSICAL_ARTIST_DAO] All MusicalArtist found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[MUSICAL_ARTIST_DAO] Error searching for all MusicalArtist. Exception " + e);
			logEx.error("Error searching for all MusicalArtist", e);
		}
	}

}
