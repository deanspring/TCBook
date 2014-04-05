package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.People;
import com.tcbook.ws.database.datasource.DataSourceType;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public People find(Long id) {
        //TODO
        return null;
    }

    @Override
    public void insert(People people) {
        //TODO
    }

    @Override
    public void remove(final Long id) {
        //TODO
    }
}
