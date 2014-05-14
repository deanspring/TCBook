package com.tcbook.ws.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.tcbook.ws.bean.City;
import com.tcbook.ws.util.TCBookConstants;

public class CityDAOImpl extends DAO implements CityDAO {

	private static CityDAOImpl instance;

	private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
	private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

	private CityDAOImpl() {
		super();
	}

	public static CityDAOImpl getInstance() {
		if (instance == null) {
			instance = new CityDAOImpl();
		}
		return instance;
	}

	public City findForID(Long idCity) {
		City result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM Cidade WHERE id = ?");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), idCity);

			if (rows != null && rows.size() > 0) {
				result = new City();
				Map<String, Object> row = rows.get(0);

				result.setId(new Long((Integer) row.get("id")));
				result.setName(new String(row.get("nome_cidade").toString()));
			}

			log.info("[CITY] City for id {} found in database in " + (System.currentTimeMillis() - before) + "ms", idCity);
		} catch (Exception e) {
			log.error("[CITY] Error searching city for id {}. Exception " + e, idCity);
			logEx.error("Error searching city", e);
		}

		return result;
	}

	public City findForName(String cityName) {
		City result = null;
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM Cidade WHERE nome_cidade = ?");

			long before = System.currentTimeMillis();
			List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), cityName);

			if (rows != null && rows.size() > 0) {
				result = new City();
				Map<String, Object> row = rows.get(0);

				result.setId(new Long((Integer) row.get("id")));
				result.setName(new String(row.get("nome_cidade").toString()));
			}

			log.info("[CITY] City for name {} found in database in " + (System.currentTimeMillis() - before) + "ms", cityName);
		} catch (Exception e) {
			log.error("[CITY] Error searching city for name {}. Exception " + e, cityName);
			logEx.error("Error searching city", e);
		}

		return result;
	}

	@Override
	public void insert(final City city) throws SQLException {
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO Cidade");
			sb.append(" (nome_cidade)");
			sb.append(" VALUES (?)");

			long before = System.currentTimeMillis();
			getJdbc().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());
					int i = 1;

					ps.setString(i++, city.getName());

					return ps;
				}
			});
			log.info("[CITY_DAO] City {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", city);
		} catch (Exception e) {
			log.error("[CITY_DAO] Error persisting City {}. Exception " + e, city);
			logEx.error("Error persisting City", e);
		}
	}

	@Override
	public void remove(final Long idCity) throws SQLException {
		try {
			final StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM Cidade WHERE id = ?");

			long before = System.currentTimeMillis();
			getJdbc().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

					PreparedStatement ps = connection.prepareStatement(sb.toString());

					ps.setInt(1, idCity.intValue());

					return ps;
				}
			});
			log.info("[CITY_DAO] City id: {} removed from database in " + (System.currentTimeMillis() - before) + "ms", idCity);
		} catch (Exception e) {
			log.error("[CITY_DAO] Error removing City id: {}. Exception " + e, idCity);
			logEx.error("Error removing City", e);
		}
	}

}
