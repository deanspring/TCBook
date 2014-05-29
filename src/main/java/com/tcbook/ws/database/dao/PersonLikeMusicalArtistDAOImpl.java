package com.tcbook.ws.database.dao;

import com.tcbook.ws.util.Pair;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.sql.Date;
import java.util.*;

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
					new String[] { "UPDATE PessoaCurteArtistaMusical p SET p.mbid = (SELECT mbid FROM ArtistaMusical a WHERE a.id = p.id_artista_musical);",
							"UPDATE PessoaCurteArtistaMusical p SET p.id_artista_musical = (SELECT id FROM ArtistaMusical a WHERE a.mbid = p.mbid LIMIT 1) WHERE p.mbid IS NOT NULL;",
							"UPDATE GeneroArtistaMusical g SET g.mbid = (SELECT mbid FROM ArtistaMusical a WHERE a.id = g.id_artista_musical);",
							"UPDATE GeneroArtistaMusical g SET g.id_artista_musical = (SELECT id FROM ArtistaMusical a WHERE a.mbid = g.mbid LIMIT 1) WHERE g.mbid IS NOT NULL;" });

			log.info("[MUSICAL_ARTIST_DAO] All MusicalArtist found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[MUSICAL_ARTIST_DAO] Error searching for all MusicalArtist. Exception " + e);
			logEx.error("Error searching for all MusicalArtist", e);
		}
	}

	public Map<String, Float> generalRatings() {
		Map<String, Float> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT AVG(nota) as media, STD(nota) AS desvio FROM PessoaCurteArtistaMusical;");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
			result = new HashMap<String, Float>();

			for (Map<String, Object> row : rows) {
				result.put("media", Float.valueOf(row.get("media").toString()));
				result.put("desvio", Float.valueOf(row.get("desvio").toString()));
			}

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] General ratings found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for general ratings. Exception " + e);
			logEx.error("Error searching for general ratings", e);
		}

		return result;
	}

	public Map<Long, Float> averageByArtist() {
		Map<Long, Float> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT id_artista_musical, AVG(nota) AS media FROM PessoaCurteArtistaMusical " + "GROUP BY id_artista_musical " + "ORDER BY AVG(nota) DESC " + "LIMIT 20;");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
			result = new LinkedHashMap<Long, Float>();

			for (Map<String, Object> row : rows) {
				result.put(Long.valueOf(row.get("id_artista_musical").toString()), Float.valueOf(row.get("media").toString()));
			}

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Average by artist found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for average by artist. Exception " + e);
			logEx.error("Error searching for average by artist", e);
		}

		return result;
	}

	@Override
	public Map<Long, Float> topTwentyAveragesByArtist() {
		Map<Long, Float> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT id_artista_musical, AVG(nota) AS media FROM PessoaCurteArtistaMusical " + "GROUP BY id_artista_musical " + "HAVING COUNT(1) >= 2 " + "ORDER BY AVG(nota) DESC "
					+ "LIMIT 20;");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
			result = new LinkedHashMap<Long, Float>();

			for (Map<String, Object> row : rows) {
				result.put(Long.valueOf(row.get("id_artista_musical").toString()), Float.valueOf(row.get("media").toString()));
			}

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Top twenty averages by artist found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for top twenty averages by artist. Exception " + e);
			logEx.error("Error searching for top twenty averages by artist", e);
		}

		return result;
	}

	@Override
	public Map<Long, Integer> topTenPopularArtists() {
		Map<Long, Integer> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT id_artista_musical, COUNT(1) AS ocorrencias FROM PessoaCurteArtistaMusical " + "GROUP BY id_artista_musical " + "ORDER BY COUNT(1) DESC " + "LIMIT 10;");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
			result = new LinkedHashMap<Long, Integer>();

			for (Map<String, Object> row : rows) {
				result.put(Long.valueOf(row.get("id_artista_musical").toString()), Integer.valueOf(row.get("ocorrencias").toString()));
			}

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Top ten popular artists found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for top ten popular artists. Exception " + e);
			logEx.error("Error searching for top ten popular artists", e);
		}

		return result;
	}

	@Override
	public Map<Long, Float> topTenStandardDeviationByArtist() {
		Map<Long, Float> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT id_artista_musical, STD(nota) AS desvio FROM PessoaCurteArtistaMusical " + "GROUP BY id_artista_musical " + "HAVING COUNT(1) >= 2 " + "ORDER BY STD(nota) DESC "
					+ "LIMIT 10;");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
			result = new LinkedHashMap<Long, Float>();

			for (Map<String, Object> row : rows) {
				result.put(Long.valueOf(row.get("id_artista_musical").toString()), Float.valueOf(row.get("desvio").toString()));
			}

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Top ten standard deviation by artist found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for top ten standard deviation by artist. Exception " + e);
			logEx.error("Error searching for top ten standard deviation by artist", e);
		}

		return result;
	}

	@Override
	public Map<Long, Integer> artistsPopularity() {
		Map<Long, Integer> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT id_artista_musical, COUNT(1) AS ocorrencias FROM PessoaCurteArtistaMusical " + "GROUP BY id_artista_musical " + "ORDER BY ocorrencias DESC;");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
			result = new LinkedHashMap<Long, Integer>();

			for (Map<String, Object> row : rows) {
				result.put(Long.valueOf(row.get("id_artista_musical").toString()), Integer.valueOf(row.get("ocorrencias").toString()));
			}

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Artists popularity found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for artists popularity. Exception " + e);
			logEx.error("Error searching for artists popularity ", e);
		}

		return result;
	}

	@Override
	public Map<Integer, Integer> peopleLikesByAmount() {
		Map<Integer, Integer> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT tmp.ocorrencias as quantas_curtidas, count(1) as ocorrencias FROM "
					+ "(SELECT COUNT(1) AS ocorrencias,id_pessoa FROM PessoaCurteArtistaMusical GROUP BY id_Pessoa) AS tmp " + "GROUP BY quantas_curtidas " + "ORDER BY quantas_curtidas;");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
			result = new LinkedHashMap<Integer, Integer>();

			for (Map<String, Object> row : rows) {
				result.put(Integer.valueOf(row.get("quantas_curtidas").toString()), Integer.valueOf(row.get("ocorrencias").toString()));
			}

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] People likes by amount found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for people likes by amount. Exception " + e);
			logEx.error("Error searching for people likes by amount", e);
		}

		return result;
	}

	@Override
	public Map<Integer, Integer> artistsLikesByAmount() {
		Map<Integer, Integer> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT tmp.ocorrencias as quantas_curtidas, count(1) as ocorrencias FROM "
					+ "(SELECT COUNT(1) as ocorrencias,id_artista_musical FROM PessoaCurteArtistaMusical GROUP BY id_artista_musical) AS tmp " + "GROUP BY quantas_curtidas "
					+ "ORDER BY quantas_curtidas;");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
			result = new LinkedHashMap<Integer, Integer>();

			for (Map<String, Object> row : rows) {
				result.put(Integer.valueOf(row.get("quantas_curtidas").toString()), Integer.valueOf(row.get("ocorrencias").toString()));
			}

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Artists likes by amount found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for artists likes by amount. Exception " + e);
			logEx.error("Error searching for artists likes by amount", e);
		}

		return result;
	}

	@Override
	public List<Long> artistsForPersonWithMinimumRate(Long personId, Integer minimumRate) {
		List<Long> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("select id_artista_musical from PessoaCurteArtistaMusical where nota >= ? and id_Pessoa=? limit 15");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), minimumRate, personId);
			result = new ArrayList<Long>();

			for (Map<String, Object> row : rows) {
				result.add(new Long((Integer) row.get("id_artista_musical")));
			}

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Artists with rate at minimum {} for person {} found in database in " + (System.currentTimeMillis() - before) + "ms", minimumRate, personId);
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for artists with rate at minimum {} for person {} ", minimumRate, personId);
			logEx.error("Error searching for artists with minimum likes", e);
		}

		return result;
	}

	@Override
	public Map<Long, Double> averagesForArtists(final List<Long> artists) {
		Map<Long, Double> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("select id_artista_musical, avg(nota) as media from PessoaCurteArtistaMusical where id_artista_musical in (" + getListOfQueryParam(artists)
					+ ") group by id_artista_musical order by media desc");

			long before = System.currentTimeMillis();

			List<Pair<Long, Double>> queryResult = getJdbc().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());
					int i = 1;

					for (Long l : artists) {
						ps.setInt(i++, l.intValue());
					}

					return ps;
				}
			}, new RowMapper<Pair<Long, Double>>() {
				@Override
				public Pair<Long, Double> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Pair<Long, Double> pair = new Pair<Long, Double>();
					pair.setLeft(rs.getLong("id_artista_musical"));
					pair.setRight(rs.getDouble("media"));
					return pair;
				}
			});

			if (queryResult != null && !queryResult.isEmpty()) {
				result = new HashMap<Long, Double>();
			}

			for (Pair<Long, Double> pair : queryResult) {
				result.put(pair.getLeft(), pair.getRight());
			}

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Averages for artists found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for artists ratings averages. Exception " + e);
			logEx.error("Error searching for searching artists ratings averages", e);
		}

		return result;
	}

	@Override
	public Map<Long, Integer> artistsLikesByFriends(final List<Long> artists, final Long personId) {
		Map<Long, Integer> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("select id_artista_musical, count(1) as ocorrencias from PessoaCurteArtistaMusical where " + "id_artista_musical in (" + getListOfQueryParam(artists) + ") and "
					+ "id_pessoa in (select id_conhecido from Conhece where id_Pessoa=?) " + "group by id_artista_musical");

			long before = System.currentTimeMillis();

			List<Pair<Long, Integer>> queryResult = getJdbc().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());
					int i = 1;

					for (Long l : artists) {
						ps.setInt(i++, l.intValue());
					}

					ps.setInt(i++, personId.intValue());

					return ps;
				}
			}, new RowMapper<Pair<Long, Integer>>() {
				@Override
				public Pair<Long, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Pair<Long, Integer> pair = new Pair<Long, Integer>();
					pair.setLeft(rs.getLong("id_artista_musical"));
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

			log.info("[PERSON_LIKE_MUSICAL_ARTIST] Artists likes for friends found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[PERSON_LIKE_MUSICAL_ARTIST] Error searching for artists likes for friends. Exception " + e);
			logEx.error("Error searching for artists likes for friends", e);
		}

		return result;
	}

	protected String getListOfQueryParam(final List<Long> artists) {
		String parameters = "";

		for (int i = 0; i < artists.size(); i++)
			parameters += "?,";

		return parameters.substring(0, parameters.length() - 1);
	}
}
