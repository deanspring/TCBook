package com.tcbook.ws.database.dao;

import com.tcbook.ws.database.datasource.DataSourceManager;
import com.tcbook.ws.database.datasource.DataSourceType;
import com.tcbook.ws.util.TCBookProperties;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public abstract class DAO {

	private static final String DB_ALIAS = "TCBOOK_DB";

	private JdbcTemplate jdbcTemplate;

	public DAO() {
		DataSourceManager dataSourceManager = DataSourceManager.getInstance();
		DataSource dataSource = dataSourceManager.getDataSource(getDatabaseAlias(), getDataSourceType());
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	protected String getDatabaseAlias() {
		return DB_ALIAS;
	}

	protected DataSourceType getDataSourceType() {
		return DataSourceType.valueOf(TCBookProperties.getInstance().getString("tcbook.db.type"));
	}

	protected JdbcTemplate getJdbc() {
		return jdbcTemplate;
	}
}
