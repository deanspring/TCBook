package com.tcbook.ws.database.datasource;

import com.tcbook.ws.util.TCBookConstants;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceManager {

    private Logger log = LoggerFactory.getLogger(DataSourceManager.class);
    private Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private static final String PROP_DRIVERCLASSNAME = "driverClassName";
    private static final String PROP_URL = "url";
    private static final String PROP_USERNAME = "username";
    private static final String PROP_PASSWORD = "password";
    private static final String PROP_MAXACTIVE = "maxActive";
    private static final String PROP_MAXIDLE = "maxIdle";
    private static final String PROP_MINIDLE = "minIdle";
    private static final String PROP_INITIALSIZE = "initialSize";
    private static final String PROP_MAXWAIT = "maxWait";
    private static final String PROP_VALIDATIONQUERY = "validationQuery";

    private static DataSourceManager instance;

    private Map<DataSourceType, Map<String, DataSource>> dataSourceMap;

    /**
     * private constructor
     */
    private DataSourceManager() {
        dataSourceMap = new ConcurrentHashMap<DataSourceType, Map<String, DataSource>>();
    }

    /**
     * @return DataSourceManager instance
     */
    public static synchronized DataSourceManager getInstance() {
        if (instance == null) {
            instance = new DataSourceManager();
        }
        return instance;
    }

    /**
     * Init a database connection pool (Datasource)
     *
     * @param databaseAlias        database alias
     * @param dataSourceProperties connection and pool properties
     */
    public void initDataSource(String databaseAlias, DataSourceProperties dataSourceProperties) {
        try {
            Properties properties = new Properties();
            properties.setProperty(PROP_DRIVERCLASSNAME, dataSourceProperties.getDriver());
            properties.setProperty(PROP_URL, dataSourceProperties.getUrl());
            properties.setProperty(PROP_USERNAME, dataSourceProperties.getUsername());
            properties.setProperty(PROP_PASSWORD, dataSourceProperties.getPassword());
            if (dataSourceProperties.getMaxActive() != null) {
                properties.setProperty(PROP_MAXACTIVE, String.valueOf(dataSourceProperties.getMaxActive()));
            }
            if (dataSourceProperties.getMaxIdle() != null) {
                properties.setProperty(PROP_MAXIDLE, String.valueOf(dataSourceProperties.getMaxIdle()));
            }
            if (dataSourceProperties.getMinIdle() != null) {
                properties.setProperty(PROP_MINIDLE, String.valueOf(dataSourceProperties.getMinIdle()));
            }
            if (dataSourceProperties.getInitialSize() != null) {
                properties.setProperty(PROP_INITIALSIZE, String.valueOf(dataSourceProperties.getInitialSize()));
            }
            if (dataSourceProperties.getMaxWait() != null) {
                properties.setProperty(PROP_MAXWAIT, String.valueOf(dataSourceProperties.getMaxWait()));
            }
            if (dataSourceProperties.getValidationQuery() != null) {
                properties.setProperty(PROP_VALIDATIONQUERY, dataSourceProperties.getValidationQuery());
            }

            DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);

            Map<String, DataSource> dataSourceMapForType = dataSourceMap.get(dataSourceProperties.getType());
            if (dataSourceMapForType == null) {
                dataSourceMapForType = new ConcurrentHashMap<String, DataSource>();
            }
            dataSourceMapForType.put(databaseAlias, dataSource);
            dataSourceMap.put(dataSourceProperties.getType(), dataSourceMapForType);

            log.info("DataSource " + databaseAlias + " created with properties: " + dataSourceProperties);

        } catch (Exception e) {
            logEx.error("Problem on create database connection pool (DataSource): ", e);
            log.error("Problem on create database connection pool (DataSource): " + e.toString() + ". Data: " + dataSourceProperties);

            throw new RuntimeException("Problem on create database connection pool (DataSource)", e);
        }

    }

    /**
     * Get datasource
     *
     * @param databaseAlias  database alias
     * @param dataSourceType the type of the data source @see DataSourceType
     * @return DataSource
     */
    public DataSource getDataSource(String databaseAlias, DataSourceType dataSourceType) {
        Map<String, DataSource> dataSourceForType = dataSourceMap.get(dataSourceType);
        if (dataSourceForType != null) {
            return dataSourceForType.get(databaseAlias);
        }
        return null;
    }

    /**
     * Close connection pool (DataSource)
     *
     * @param databaseAlias database alias
     */
    public void closeDataSource(String databaseAlias, DataSourceType dataSourceType) {
        Map<String, DataSource> dataSourceForType = dataSourceMap.get(dataSourceType);
        if (dataSourceForType != null) {
            BasicDataSource basicDataSource = (BasicDataSource) dataSourceForType.get(databaseAlias);
            try {
                basicDataSource.close();
            } catch (SQLException e) {
                logEx.error("Problem on close database connection pool (DataSource): ", e);
                log.error("Problem on close database connection pool (DataSource)");
            }
        }
    }

}
