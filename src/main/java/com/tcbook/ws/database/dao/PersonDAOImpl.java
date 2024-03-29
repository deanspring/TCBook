package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.Person;
import com.tcbook.ws.util.TCBookConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PersonDAOImpl extends DAO implements PersonDAO {

    private static PersonDAOImpl instance;

    private static Logger log = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_DAO);
    private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private PersonDAOImpl() {
        super();
    }

    public static PersonDAOImpl getInstance() {
        if (instance == null) {
            instance = new PersonDAOImpl();
        }
        return instance;
    }

    @Override
    public Person find(final Long id) {
        Person result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM Pessoa");
            sb.append(" WHERE id=? AND ativa = 1 LIMIT 1");

            long before = System.currentTimeMillis();
            result = (Person) getJdbc().queryForObject(sb.toString(), new Object[]{id}, new PersonRowMapper());
            log.info("[person_DAO] Person {} found in database in " + (System.currentTimeMillis() - before) + "ms", result);
        } catch (Exception e) {
            log.error("[person_DAO] Error searching Person id: {}. Exception " + e, id);
            logEx.error("Error searching Person", e);
        }
        return result;
    }

    @Override
    public List<Person> findAll() {
        List<Person> result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM Pessoa WHERE ativa = 1");

            long before = System.currentTimeMillis();
            List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
            result = new ArrayList<Person>();

            for (Map<String, Object> row : rows) {
                Person person = new Person();
                person.setId(new Long((Integer) row.get("id")));
                person.setName(row.get("nome").toString());
                person.setUrl(row.get("uri").toString());
                person.setLogin(row.get("login").toString());
                person.setHometown(row.get("cidade_natal") != null ? row.get("cidade_natal").toString() : null);
                person.setEnabled(Boolean.valueOf(row.get("ativa").toString()));
                result.add(person);
            }

            log.info("[person_DAO] All Person found in database in " + (System.currentTimeMillis() - before) + "ms");
        } catch (Exception e) {
            log.error("[person_DAO] Error searching for all Person. Exception " + e);
            logEx.error("Error searching for all Person", e);
        }
        return result;
    }

    @Override
    public void insert(final Person person) throws SQLException {
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

                    ps.setString(i++, person.getLogin());

                    ps.setString(i++, person.getUrl());

                    ps.setString(i++, person.getName());

                    if (StringUtils.isBlank(person.getHometown())) {
                        ps.setNull(i++, Types.VARCHAR);
                    } else {
                        ps.setString(i++, person.getHometown());
                    }

                    return ps;
                }
            });
            log.info("[person_DAO] Person {} inserted in database in " + (System.currentTimeMillis() - before) + "ms", person);
        } catch (Exception e) {
            log.error("[person_DAO] Error persisting Person {}. Exception " + e, person);
            logEx.error("Error persisting Person", e);
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
            log.info("[person_DAO] Person id: {} removed from database in " + (System.currentTimeMillis() - before) + "ms", id);
        } catch (Exception e) {
            log.error("[person_DAO] Error removing Person id: {}. Exception " + e, id);
            logEx.error("Error removing Person", e);
        }
    }

    @Override
    public void removeColleaguesForPerson(final Long idPerson) throws SQLException {
        final StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM conhece WHERE id_pessoa = ? OR id_conhecido = ?");

        long before = System.currentTimeMillis();
        getJdbc().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                PreparedStatement ps = connection.prepareStatement(sb.toString());
                int i = 1;

                ps.setInt(i++, idPerson.intValue());

                ps.setInt(i++, idPerson.intValue());

                return ps;
            }
        });
        log.info("[person_DAO] Person id: {} colleagues deleted from database in " + (System.currentTimeMillis() - before) + "ms", idPerson);
    }

    @Override
    public void removeBlockingsForPerson(final Long idPerson) {
        final StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM bloqueio WHERE id_pessoa = ? OR id_bloqueado = ?");

        long before = System.currentTimeMillis();
        getJdbc().update(new PreparedStatementCreator() {
                             public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                                 PreparedStatement ps = connection.prepareStatement(sb.toString());
                                 int i = 1;

                                 ps.setInt(i++, idPerson.intValue());

                                 ps.setInt(i++, idPerson.intValue());

                                 return ps;
                             }
                         }
        );
        log.info("[person_DAO] Person id: {} blockings deleted from database in " + (System.currentTimeMillis() - before) + "ms", idPerson);
    }

    public void update(final Person person) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("UPDATE Pessoa");
            sb.append(" SET login=?,");
            sb.append("uri=?, ");
            sb.append("nome=?, ");
            sb.append("cidade_natal=?, ");
            sb.append("ativa=? ");
            sb.append(" WHERE id=?");

            long before = System.currentTimeMillis();
            getJdbc().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    int i = 1;

                    ps.setString(i++, person.getLogin());
                    ps.setString(i++, person.getUrl());
                    ps.setString(i++, person.getName());
                    ps.setString(i++, person.getHometown());
                    ps.setBoolean(i++, person.getEnabled());
                    ps.setLong(i++, person.getId());

                    return ps;
                }
            });
            log.info("[person_DAO] Person {} updated in " + (System.currentTimeMillis() - before) + "ms", person);
        } catch (Exception e) {
            log.error("[person_DAO] Error updating Person {}. Exception " + e, person);
            logEx.error("Error updating Person", e);
        }
    }

    @Override
    public Map<Long, Integer> topTenEclecticPeople() {
        Map<Long, Integer> result = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("SELECT p.id, count(1) as numero_generos from Pessoa p, GeneroMusical g where g.id in ( " +
                    	"SELECT ga.id_genero from GeneroArtistaMusical ga where ga.id_artista_musical in ( " +
                    		"SELECT pc.id_artista_musical from PessoaCurteArtistaMusical pc where pc.id_Pessoa = p.id " +
                    		") " +
                    	") " +
                    "GROUP BY p.id " +
                    "ORDER BY numero_generos desc LIMIT 10;");

            long before = System.currentTimeMillis();
            List<Map<String, Object>> rows = getJdbc().queryForList(sb.toString());
            result = new LinkedHashMap<Long, Integer>();

            for (Map<String, Object> row : rows) {
                result.put(Long.valueOf(row.get("id").toString()), Integer.valueOf(row.get("numero_generos").toString()));
            }

            log.info("[person_DAO] Top ten eclectic people found in database in " + (System.currentTimeMillis() - before) + "ms");
        } catch (Exception e) {
            log.error("[person_DAO] Error searching for top ten eclectic people. Exception " + e);
            logEx.error("Error searching for top ten eclectic people", e);
        }

        return result;
    }

    private class PersonRowMapper implements RowMapper<Object> {

        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {

            Person person = new Person();
            person.setId(new Long(resultSet.getInt("id")));
            person.setLogin(resultSet.getString("login"));
            person.setName(resultSet.getString("nome"));
            person.setUrl(resultSet.getString("uri"));
            person.setHometown(resultSet.getString("cidade_natal"));
            person.setEnabled(resultSet.getBoolean("ativa"));

            return person;
        }
    }

}
