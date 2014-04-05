package com.tcbook.ws.database.dao;

import com.tcbook.ws.database.datasource.DataSourceManager;
import com.tcbook.ws.database.datasource.DataSourceType;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public abstract class DAO {
    private JdbcTemplate jdbcTemplate;

    public DAO() {
        DataSourceManager dataSourceManager = DataSourceManager.getInstance();
        DataSource dataSource = dataSourceManager.getDataSource(getDatabaseAlias(), getDataSourceType());
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Return the alias of the database
     *
     * @return
     */
    protected abstract String getDatabaseAlias();

    /**
     * Return the type of the dataSource
     *
     * @return
     */
    protected abstract DataSourceType getDataSourceType();

    protected JdbcTemplate getJdbc() {
        return jdbcTemplate;
    }
}
