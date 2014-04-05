package com.tcbook.ws.database.datasource;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DataSourceProperties {

    private DataSourceType type;
    private String databaseSchemaName;
    private String driver;
    private String url;
    private String username;
    private String password;
    private Integer initialSize;
    private Integer maxActive;
    private Integer minIdle;
    private Integer maxIdle;
    private Integer maxWait;
    private String validationQuery;

    /**
     * @return the databaseSchemaName
     */
    public String getDatabaseSchemaName() {
        return databaseSchemaName;
    }

    /**
     * @param databaseSchemaName the databaseSchemaName to set
     */
    public void setDatabaseSchemaName(String databaseSchemaName) {
        this.databaseSchemaName = databaseSchemaName;
    }

    /**
     * @return the driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @param driver the driver to set
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the initialSize
     */
    public Integer getInitialSize() {
        return initialSize;
    }

    /**
     * @param initialSize the initialSize to set
     */
    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    /**
     * @return the maxActive
     */
    public Integer getMaxActive() {
        return maxActive;
    }

    /**
     * @param maxActive the maxActive to set
     */
    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    /**
     * @return the minIdle
     */
    public Integer getMinIdle() {
        return minIdle;
    }

    /**
     * @param minIdle the minIdle to set
     */
    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    /**
     * @return the maxIdle
     */
    public Integer getMaxIdle() {
        return maxIdle;
    }

    /**
     * @param maxIdle the maxIdle to set
     */
    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    /**
     * @return the maxWait
     */
    public Integer getMaxWait() {
        return maxWait;
    }

    /**
     * @param maxWait the maxWait to set
     */
    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    /**
     * @return the validationQuery
     */
    public String getValidationQuery() {
        return validationQuery;
    }

    /**
     * @param validationQuery the validationQuery to set
     */
    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    /**
     * @return the type
     */
    public DataSourceType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(DataSourceType type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
