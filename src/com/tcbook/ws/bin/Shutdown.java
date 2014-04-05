package com.tcbook.ws.bin;

import com.tcbook.ws.database.datasource.DataSourceManager;
import com.tcbook.ws.database.datasource.DataSourceType;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shutdown extends Thread {

    private static Logger log = LoggerFactory.getLogger(Shutdown.class);
    private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    /*
     * (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        Long before = System.currentTimeMillis();
        log.info("TCBookMain is shutting down...");

        try {
            // stop web server
            TCBookMain.getWebServer().shutdown();
        } catch (Exception e) {
            logEx.error("Error shutting down WebServer.", e);
        }

        try {
            // stop DataSource connection
            DataSourceManager.getInstance().closeDataSource(TCBookMain.DATABASE_ALIAS,
                    DataSourceType.valueOf(TCBookProperties.getInstance().getString("tcbook.db.type")));
        } catch (Exception e) {
            logEx.error("Error shutting down DataSource connection.", e);
        }

        log.info("TCBook Ended. Time it took: " + (System.currentTimeMillis() - before) + "ms.");
    }
}