package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.Country;
import com.tcbook.ws.database.datasource.DataSourceType;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by caiouvini on 5/13/14.
 */
public class CountryDAOImpl extends DAO implements CountryDAO {

    private static CountryDAOImpl instance;

    private static final String DB_ALIAS = "TCBOOK_DB";

    private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
    private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private CountryDAOImpl() {
        super();
    }

    public static CountryDAOImpl getInstance() {
        if (instance == null) {
            instance = new CountryDAOImpl();
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

    public Country findForID(Long idCountry) {
        Country result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM Pais WHERE id = ?");

            long before = System.currentTimeMillis();
            List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), idCountry);

            if (rows != null && rows.size() > 0) {
                result = new Country();
                Map<String, Object> row = rows.get(0);

                result.setId(new Long((Integer) row.get("id")));
                result.setName(new String(row.get("nome_pais").toString()));
            }

            log.info("[COUNTRY_DAO] Country for id {} found in database in " + (System.currentTimeMillis() - before) + "ms", idCountry);
        } catch (Exception e) {
            log.error("[COUNTRY_DAO] Error searching country for id {}. Exception " + e, idCountry);
            logEx.error("Error searching city", e);
        }

        return result;
    }

    public Country findForName(String countryName) {
        Country result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM Pais WHERE nome_pais = ?");

            long before = System.currentTimeMillis();
            List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), countryName);

            if (rows != null && rows.size() > 0) {
                result = new Country();
                Map<String, Object> row = rows.get(0);

                result.setId(new Long((Integer) row.get("id")));
                result.setName(new String(row.get("nome_pais").toString()));
            }

            log.info("[COUNTRY_DAO] Country for name {} found in database in " + (System.currentTimeMillis() - before) + "ms", countryName);
        } catch (Exception e) {
            log.error("[COUNTRY_DAO] Error searching country for name {}. Exception " + e, countryName);
            logEx.error("Error searching country", e);
        }

        return result;
    }

    @Override
    public void insert(final Country country) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO Pais");
            sb.append(" (nome_pais)");
            sb.append(" VALUES (?)");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    int i = 1;

                    ps.setString(i++, country.getName());

                    return ps;
                }
            });
            log.info("[COUNTRY_DAO] Country {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", country);
        } catch (Exception e) {
            log.error("[COUNTRY_DAO] Error persisting Country {}. Exception " + e, country);
            logEx.error("Error persisting Country", e);
        }
    }

    @Override
    public void remove(final Long idCountry) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM Pais WHERE id = ?");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());

                    ps.setInt(1, idCountry.intValue());

                    return ps;
                }
            });
            log.info("[COUNTRY_DAO] Country id: {} removed from database in " + (System.currentTimeMillis() - before) + "ms", idCountry);
        } catch (Exception e) {
            log.error("[COUNTRY_DAO] Error removing Country id: {}. Exception " + e, idCountry);
            logEx.error("Error removing Country", e);
        }
    }

}
