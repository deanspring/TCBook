package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.MusicalGenre;
import com.tcbook.ws.database.datasource.DataSourceType;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MusicalGenresDAOImpl extends DAO implements MusicalGenresDAO {

	private static MusicalGenresDAOImpl instance;

	private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
	private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

	private MusicalGenresDAOImpl() {
		super();
	}

	public static MusicalGenresDAOImpl getInstance() {
		if (instance == null) {
			instance = new MusicalGenresDAOImpl();
		}
		return instance;
	}

	public void insert(final String genreName) throws SQLException {
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO GeneroMusical");
			sb.append(" (nome_genero)");
			sb.append(" VALUES (?)");

			long before = System.currentTimeMillis();
			getJdbc().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());
					int i = 1;

					ps.setString(i++, genreName);

					return ps;
				}
			});
			log.info("[MUSICAL_GENRES] Genre {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", genreName);
		} catch (Exception e) {
			log.error("[MUSICAL_GENRES] Error persisting genre {}. Exception " + e, genreName);
			logEx.error("Error persisting genre", e);
		}
	}

	@Override
	public MusicalGenre getForName(String genreName) throws SQLException {
		MusicalGenre result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM GeneroMusical WHERE nome_genero = ?");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), genreName);

			if (rows != null && rows.size() > 0) {
				result = new MusicalGenre();
				Map<String, Object> row = rows.get(0);

				result.setId(new Long((Integer) row.get("id")));
				result.setName(new String(row.get("nome_genero").toString()));
			}

			log.info("[MUSICAL_GENRES] Genre for name {} found in database in " + (System.currentTimeMillis() - before) + "ms", genreName);
		} catch (Exception e) {
			log.error("[MUSICAL_GENRES] Error searching genre for name {}. Exception " + e, genreName);
			logEx.error("Error searching genre", e);
		}

		return result;
	}

	@Override
	public List<MusicalGenre> findAll() {
		List<MusicalGenre> result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM GeneroMusical");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());

			if (rows != null && rows.size() > 0) {
				result = new ArrayList<MusicalGenre>();
			}

			for (Map<String, Object> row : rows) {
				MusicalGenre genre = new MusicalGenre();
				genre.setId(new Long((Integer) row.get("id")));
				genre.setName(row.get("nome_genero").toString());
				result.add(genre);
			}

			log.info("[MUSICAL_GENRES] All genres found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[MUSICAL_GENRES] Error searching for all genres. Exception " + e);
			logEx.error("Error searching for all genres", e);
		}

		return result;
	}

}
