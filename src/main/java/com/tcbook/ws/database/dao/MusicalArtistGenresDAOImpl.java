package com.tcbook.ws.database.dao;

import com.tcbook.ws.database.datasource.DataSourceType;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MusicalArtistGenresDAOImpl extends DAO implements MusicalArtistGenresDAO {

	private static MusicalArtistGenresDAOImpl instance;

	private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
	private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

	private MusicalArtistGenresDAOImpl() {
		super();
	}

	public static MusicalArtistGenresDAOImpl getInstance() {
		if (instance == null) {
			instance = new MusicalArtistGenresDAOImpl();
		}
		return instance;
	}

	public void insert(final Long idMusicalArtist, final Long idGenre) throws SQLException {
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO GeneroArtistaMusical");
			sb.append(" (id_artista_musical,");
			sb.append(" id_genero)");
			sb.append(" VALUES (?,?)");

			long before = System.currentTimeMillis();
			getJdbc().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());
					int i = 1;

					ps.setInt(i++, idMusicalArtist.intValue());

					ps.setInt(i++, idGenre.intValue());

					return ps;
				}
			});
			log.info("[MUSICAL_ARTIST_GENRES] MusicalArtist {} Genre {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", idMusicalArtist, idGenre);
		} catch (Exception e) {
			log.error("[MUSICAL_ARTIST_GENRES] Error persisting MusicalArtist {} genre {}. Exception " + e, idMusicalArtist, idGenre);
			logEx.error("Error persisting musical artist genre", e);
		}
	}

	public void removeAllForArtist(final Long idMusicalArtist) throws SQLException {
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM GeneroArtistaMusical WHERE ");
			sb.append("id_artista_musical = ?");

			long before = System.currentTimeMillis();
			getJdbc().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());

					ps.setInt(1, idMusicalArtist.intValue());

					return ps;
				}
			});
			log.info("[MUSICAL_ARTIST_GENRES] Genres for MusicalArtist {} removed from database in " + (System.currentTimeMillis() - before) + "ms", idMusicalArtist);
		} catch (Exception e) {
			log.error("[MUSICAL_ARTIST_GENRES] Error removing genres for MusicalArtist {}. Exception " + e, idMusicalArtist);
			logEx.error("Error removing genres for MusicalArtist", e);
		}
	}

}
