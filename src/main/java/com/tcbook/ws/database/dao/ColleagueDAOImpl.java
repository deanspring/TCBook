package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.ColleagueMusicalArtistSharingInfo;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ColleagueDAOImpl extends DAO implements ColleagueDAO {

    private static ColleagueDAOImpl instance;

    private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
    private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private ColleagueDAOImpl() {
        super();
    }

    public static ColleagueDAOImpl getInstance() {
        if (instance == null) {
            instance = new ColleagueDAOImpl();
        }
        return instance;
    }

    public List<Long> findAllForId(final Long id) {
        List<Long> result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM Conhece WHERE id_pessoa = " + id);

            long before = System.currentTimeMillis();
            List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
            result = new ArrayList<Long>();

            for (Map<String, Object> row : rows) {
                result.add(new Long((Integer) row.get("id_conhecido")));
            }

            log.info("[COLLEAGUE_DAO] All Colleagues for id {} found in database in " + (System.currentTimeMillis() - before) + "ms", id);
        } catch (Exception e) {
            log.error("[COLLEAGUE_DAO] Error searching for all colleagues for id {}. Exception " + e, id);
            logEx.error("Error searching for all colleagues", e);
        }
        return result;
    }

    public void insert(final Long idPerson, final Long idColleague) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO Conhece");
            sb.append(" (id_pessoa,");
            sb.append("id_conhecido)");
            sb.append(" VALUES (?,?)");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    int i = 1;

                    ps.setInt(i++, idPerson.intValue());
                    ps.setInt(i++, idColleague.intValue());

                    return ps;
                }
            });
            log.info("[COLLEAGUE_DAO] Person {} and Person {} colleague's inserted in database in " + (System.currentTimeMillis() - before) + "ms", idPerson, idColleague);
        } catch (Exception e) {
            log.error("[COLLEAGUE_DAO] Error persisting Person {} and Person {} colleague's. Exception " + e, idPerson, idColleague);
            logEx.error("Error persisting colleague", e);
        }
    }

    public void remove(final Long idPerson, final Long idColleague) throws SQLException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM Conhece WHERE ");
            sb.append("id_pessoa = ? AND ");
            sb.append("id_conhecido = ?");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    int i = 1;

                    ps.setInt(i++, idPerson.intValue());

                    ps.setInt(i++, idColleague.intValue());
                    return ps;
                }
            });
            log.info("[COLLEAGUE_DAO] Person {} and Person {} colleague's removed from database in " + (System.currentTimeMillis() - before) + "ms", idPerson, idColleague);
        } catch (Exception e) {
            log.error("[COLLEAGUE_DAO] Error removing Person {} and Person {} colleague's. Exception " + e, idPerson, idColleague);
            logEx.error("Error removing colleague", e);
        }
    }

    @Override
    public List<ColleagueMusicalArtistSharingInfo> topTenKnownWithMostSharedArtists() {
        List<ColleagueMusicalArtistSharingInfo> result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("select c.id_pessoa, c.id_conhecido, count(1) as compartilhamentos from Conhece c, PessoaCurteArtistaMusical pc " +
                    "where " +
                    "c.id_pessoa = pc.id_pessoa and " +
                    "pc.id_artista_musical in ( " +
                    "select id_artista_musical from PessoaCurteArtistaMusical where id_pessoa = c.id_pessoa and id_artista_musical in ( " +
                    "select id_artista_musical from PessoaCurteArtistaMusical where id_pessoa = c.id_conhecido " +
                    ") " +
                    ") " +
                    "GROUP BY c.id_Pessoa, c.id_conhecido " +
                    "order by compartilhamentos DESC " +
                    "LIMIT 10;");

            long before = System.currentTimeMillis();
            List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
            if (rows != null && !rows.isEmpty()) {
                result = new LinkedList<ColleagueMusicalArtistSharingInfo>();
            }

            for (Map<String, Object> row : rows) {
                ColleagueMusicalArtistSharingInfo info = new ColleagueMusicalArtistSharingInfo();
                info.setIdPerson(Long.valueOf(row.get("id_pessoa").toString()));
                info.setIdColleague(Long.valueOf(row.get("id_conhecido").toString()));
                info.setAmount(Integer.valueOf(row.get("compartilhamentos").toString()));
                result.add(info);
            }

            log.info("[COLLEAGUE_DAO] Top ten colleagues with most shared artists found in database in " + (System.currentTimeMillis() - before) + "ms");
        } catch (Exception e) {
            log.error("[COLLEAGUE_DAO] Error searching for top ten colleagues with most shared artists. Exception " + e);
            logEx.error("Error searching for top ten colleagues with most shared artists", e);
        }

        return result;
    }
}
