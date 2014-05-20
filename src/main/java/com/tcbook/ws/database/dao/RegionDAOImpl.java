package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.Region;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RegionDAOImpl extends DAO implements RegionDAO {

    private static RegionDAOImpl instance;

    private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
    private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private RegionDAOImpl() {
        super();
    }

    public static RegionDAOImpl getInstance() {
        if (instance == null) {
            instance = new RegionDAOImpl();
        }
        return instance;
    }

    @Override
    public void insert(final Region region) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO Regiao");
            sb.append(" (id_cidade,");
            sb.append(" id_pais)");
            sb.append(" VALUES (?,?)");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    int i = 1;

                    ps.setInt(i++, region.getIdCity().intValue());
                    ps.setInt(i++, region.getIdCountry().intValue());

                    return ps;
                }
            });
            log.info("[REGION_DAO] Region {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", region);
        } catch (Exception e) {
            log.error("[REGION_DAO] Error persisting Region {}. Exception " + e, region);
            logEx.error("Error persisting Region", e);
        }
    }

    public Region findForCityAndCountry(String cityName, String countryName) {
        Region result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM Regiao WHERE id_cidade in (SELECT id from Cidade where nome_cidade = ?) AND id_pais in (SELECT id from Pais where nome_pais = ?)");

            long before = System.currentTimeMillis();
            List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString(), cityName, countryName);

            if (rows != null && rows.size() > 0) {
                result = new Region();
                Map<String, Object> row = rows.get(0);

                result.setId(new Long((Integer) row.get("id")));
                result.setIdCity(new Long((Integer) row.get("id_cidade")));
                result.setIdCountry(new Long((Integer) row.get("id_pais")));
            }

            log.info("[REGION_DAO] Region for city name {} and country name {} found in database in " + (System.currentTimeMillis() - before) + "ms", cityName, countryName);
        } catch (Exception e) {
            log.error("[REGION_DAO] Error searching region for city name {} and country name {}. Exception " + e, cityName);
            logEx.error("Error searching region", e);
        }

        return result;
    }

    @Override
    public Map<Long, Integer> topTenCountriesWithMostArtists() {
        Map<Long, Integer> result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("select r.id_pais, count(1) as ocorrencias from Regiao r, ArtistaMusical a WHERE " +
                    "r.id = a.id_regiao " +
                    "GROUP BY r.id_pais " +
                    "ORDER BY ocorrencias DESC LIMIT 10;");

            long before = System.currentTimeMillis();
            List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
            result = new LinkedHashMap<Long, Integer>();

            for (Map<String, Object> row : rows) {
                result.put(Long.valueOf(row.get("id_pais").toString()), Integer.valueOf(row.get("ocorrencias").toString()));
            }

            log.info("[REGION_DAO] Top ten countries with most artists found in database in " + (System.currentTimeMillis() - before) + "ms");
        } catch (Exception e) {
            log.error("[REGION_DAO] Error searching for top ten countries with most artists. Exception " + e);
            logEx.error("Error searching for top ten countries with most artists", e);
        }

        return result;
    }

}
