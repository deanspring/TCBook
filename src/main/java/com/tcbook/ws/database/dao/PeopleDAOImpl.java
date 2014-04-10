package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.People;
import com.tcbook.ws.database.datasource.DataSourceType;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PeopleDAOImpl extends DAO implements PeopleDAO {

	private static PeopleDAOImpl instance;

	private static final String DB_ALIAS = "TCBOOK_DB";

	private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
	private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

	private PeopleDAOImpl() {
		super();
	}

	public static PeopleDAOImpl getInstance() {
		if (instance == null) {
			instance = new PeopleDAOImpl();
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
	public People find(final Long id) {
		People people = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM Pessoa");
			sb.append(" WHERE id=? LIMIT 1");

			long before = System.currentTimeMillis();
			people = (People) getJdbc().queryForObject(sb.toString(), new Object[] { id }, new PeopleRowMapper());
			log.info("[PEOPLE_DAO] People {} found in database in " + (System.currentTimeMillis() - before) + "ms", people);
		} catch (Exception e) {
			log.error("[PEOPLE_DAO] Error searching People id: {}. Exception " + e, id);
			logEx.error("Error searching People", e);
		}

		return people == null ? new People() : people;
	}

	@Override
	public List<People> findAll() {
		List<People> allPeople = null;

		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM Pessoa");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
			allPeople = new ArrayList<People>();

			for (Map<String, Object> row : rows) {
				People people = new People();
				people.setId(new Long((Integer) row.get("id")));
				people.setName(row.get("nome").toString());
				people.setUrl(row.get("uri").toString());
				people.setLogin(row.get("login").toString());
				people.setHometown(row.get("cidade_natal") != null ? row.get("cidade_natal").toString() : null);
				allPeople.add(people);
			}

			log.info("[PEOPLE_DAO] All People found in database in " + (System.currentTimeMillis() - before) + "ms");
		} catch (Exception e) {
			log.error("[PEOPLE_DAO] Error searching for all People. Exception " + e);
			logEx.error("Error searching for all People", e);
		}

		return allPeople == null ? new ArrayList<People>() : allPeople;
	}

	@Override
	public void insert(final People people) throws SQLException {
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO Pessoa");
			sb.append(" (login,");
			sb.append("uri,");
			sb.append("nome,");
			sb.append("cidade_natal)");
			sb.append(" VALUES (?,?,?,?)");

			long before = System.currentTimeMillis();
			getJdbc().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());
					int i = 1;

					ps.setString(i++, people.getLogin());

					ps.setString(i++, people.getUrl());

					ps.setString(i++, people.getName());

					if (StringUtils.isBlank(people.getHometown())) {
						ps.setNull(i++, Types.VARCHAR);
					} else {
						ps.setString(i++, people.getHometown());
					}

					return ps;
				}
			});
			log.info("[PEOPLE_DAO] People {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", people);
		} catch (Exception e) {
			log.error("[PEOPLE_DAO] Error persisting People {}. Exception " + e, people);
			logEx.error("Error persisting People", e);
		}
	}

	@Override
	public void remove(final Long id) {
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM pessoa WHERE id = ?");

			long before = System.currentTimeMillis();
			getJdbc().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());

					ps.setInt(1, id.intValue());

					return ps;
				}
			});
			log.info("[PEOPLE_DAO] People id: {} removed from database in " + (System.currentTimeMillis() - before) + "ms", id);
		} catch (Exception e) {
			log.error("[PEOPLE_DAO] Error removing People id: {}. Exception " + e, id);
			logEx.error("Error removing People", e);
		}
	}

	@Override
	public void removeColleaguesForPeople(final Long id) throws SQLException {
		final StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM conhece WHERE id_pessoa = ? OR id_conhecido = ?");

		long before = System.currentTimeMillis();
		getJdbc().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

				PreparedStatement ps = connection.prepareStatement(sb.toString());
				int i = 1;

				ps.setInt(i++, id.intValue());

				ps.setInt(i++, id.intValue());

				return ps;
			}
		});
		log.info("[PEOPLE_DAO] People id: {} colleagues deleted from database in " + (System.currentTimeMillis() - before) + "ms", id);
	}

	@Override
	public void removeBlockingsForPeople(final Long id) {
		final StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM bloqueio WHERE id_pessoa = ? OR id_bloqueado = ?");

		long before = System.currentTimeMillis();
		getJdbc().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

				PreparedStatement ps = connection.prepareStatement(sb.toString());
				int i = 1;

				ps.setInt(i++, id.intValue());

				ps.setInt(i++, id.intValue());

				return ps;
			}
		});
		log.info("[PEOPLE_DAO] People id: {} blockings deleted from database in " + (System.currentTimeMillis() - before) + "ms", id);
	}

	public void update(final People people) {
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("UPDATE Pessoa");
			sb.append(" SET login=?,");
			sb.append("uri=?, ");
			sb.append("nome=?, ");
			sb.append("cidade_natal=?, ");
			sb.append(" WHERE id=?");

			long before = System.currentTimeMillis();
			getJdbc().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());
					int i = 1;

					ps.setString(i++, people.getLogin());

					ps.setString(i++, people.getUrl());

					ps.setString(i++, people.getName());

					ps.setString(i++, people.getHometown());

					return ps;
				}
			});
			log.info("[PEOPLE_DAO] People {} updated in " + (System.currentTimeMillis() - before) + "ms", people);
		} catch (Exception e) {
			log.error("[PEOPLE_DAO] Error updating People {}. Exception " + e, people);
			logEx.error("Error updating People", e);
		}
	}

	private class PeopleRowMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet resultSet, int i) throws SQLException {

			People people = new People();
			people.setId(new Long(resultSet.getInt("id")));
			people.setLogin(resultSet.getString("login"));
			people.setName(resultSet.getString("nome"));
			people.setUrl(resultSet.getString("uri"));
			people.setHometown(resultSet.getString("cidade_natal"));

			return people;
		}
	}
}
