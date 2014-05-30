package com.tcbook.ws.database.dao;

import com.tcbook.ws.util.Pair;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

	@Override
	public Map<Long, Integer> topFivePopularGenres() {
		Map<Long, Integer> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("select id_genero, count(1) as ocorrencias from GeneroArtistaMusical " + 
					"GROUP BY id_genero " +
					"ORDER BY ocorrencias DESC " + 
					"LIMIT 5;");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
			result = new LinkedHashMap<Long, Integer>();

			for (Map<String, Object> row : rows) {
				result.put(Long.valueOf(row.get("id_genero").toString()), Integer.valueOf(row.get("ocorrencias").toString()));
			}

			log.info("[MUSICAL_ARTIST_GENRES] Top five popular genres found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[MUSICAL_ARTIST_GENRES] Error searching for top five popular genres. Exception " + e);
			logEx.error("Error searching for top five popular genres", e);
		}

		return result;
	}

	@Override
	public Map<Long, Integer> getGroupedGenresForArtists(final List<Long> artists) {
		Map<Long, Integer> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("select id_genero, count(1) as ocorrencias from GeneroArtistaMusical where id_artista_musical in (" + getListOfQueryParams(artists) + ") group by id_genero");

			long before = System.currentTimeMillis();

			List<Pair<Long, Integer>> queryResult = getJdbc().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());
					int i = 1;

					for (Long l : artists) {
						ps.setInt(i++, l.intValue());
					}

					return ps;
				}
			}, new RowMapper<Pair<Long, Integer>>() {
				@Override
				public Pair<Long, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Pair<Long, Integer> pair = new Pair<Long, Integer>();
					pair.setLeft(rs.getLong("id_genero"));
					pair.setRight(rs.getInt("ocorrencias"));
					return pair;
				}
			});

			if (queryResult != null && !queryResult.isEmpty()) {
				result = new HashMap<Long, Integer>();
			}

			for (Pair<Long, Integer> pair : queryResult) {
				result.put(pair.getLeft(), pair.getRight());
			}

			log.info("[MUSICAL_ARTIST_GENRES] Popular genres for artists found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[MUSICAL_ARTIST_GENRES] Error searching for popular genres for artists. Exception " + e);
			logEx.error("Error searching for popular genres for artists", e);
		}

		return result;
	}

	protected String getListOfQueryParams(final List<Long> artists) {
		String parameters = "";
		for (int i = 0; i < artists.size(); i++) {
			parameters += "?,";
		}
		parameters = parameters.substring(0, parameters.length() - 1);
		return parameters;
	}

	@Override
	public List<Long> findGenresByArtist(Long artistId) {
		List<Long> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("select distinct(id_genero) from GeneroArtistaMusical where id_artista_musical=?");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), artistId);
			result = new ArrayList<Long>();

			for (Map<String, Object> row : rows) {
				result.add(Long.valueOf(row.get("id_genero").toString()));
			}

			log.info("[MUSICAL_ARTIST_GENRES] Genres for artists found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[MUSICAL_ARTIST_GENRES] Error searching for genres for artists. Exception " + e);
			logEx.error("Error searching for genres for artists", e);
		}

		return result;
	}

}
