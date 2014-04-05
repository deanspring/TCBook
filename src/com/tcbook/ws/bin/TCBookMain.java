package com.tcbook.ws.bin;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import com.tcbook.ws.database.datasource.DataSourceManager;
import com.tcbook.ws.database.datasource.DataSourceProperties;
import com.tcbook.ws.database.datasource.DataSourceType;
import com.tcbook.ws.util.SmartProperties;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import com.tcbook.ws.web.manager.WebServerManager;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;

/**
 * TCBookMain system main class
 *
 * @author Caio V. Uvini Palissari (caio.uvini@gmail.com)
 */
@Path("/")
public class TCBookMain {

    private static final String LOGBACK_CONFIG_FILE = "config/logback.xml";
    public static final String DATABASE_ALIAS = "TCBOOK_DB";

    private static SmartProperties tcBookProperties;
    private static WebServerManager webServer;
    private static Logger log;
    private static Logger logEx;

    public static void main(String args[]) {
        System.setProperty("logback.configurationFile", LOGBACK_CONFIG_FILE);
        start();
    }

    private static void start() {
        StopWatch chronometer = new StopWatch();
        chronometer.start();
        try {
            // load log configuration
            loadSystemLog();

            // load system configuration properties
            loadSystemProperties();

            log.debug("[TCBook] Initializing database...");
            try {
                DataSourceProperties dsProperties = new DataSourceProperties();
                dsProperties.setDriver(TCBookProperties.getInstance().getString("tcbook.db.driver"));
                dsProperties.setUrl(TCBookProperties.getInstance().getString("tcbook.db.url"));
                dsProperties.setUsername(TCBookProperties.getInstance().getString("tcbook.db.username"));
                dsProperties.setPassword(TCBookProperties.getInstance().getString("tcbook.db.password"));
                dsProperties.setInitialSize(TCBookProperties.getInstance().getInt("tcbook.db.initial.size", 5));
                dsProperties.setMaxActive(TCBookProperties.getInstance().getInt("tcbook.db.max.active", 50));
                dsProperties.setMaxIdle(TCBookProperties.getInstance().getInt("tcbook.db.max.idle", 10));
                dsProperties.setMinIdle(TCBookProperties.getInstance().getInt("tcbook.db.min.idle", 5));
                dsProperties.setMaxWait(TCBookProperties.getInstance().getInt("tcbook.db.max.wait", 60000));
                dsProperties
                        .setValidationQuery(TCBookProperties.getInstance().getString("tcbook.db.validation.query", "SELECT CURDATE();"));
                dsProperties.setType(DataSourceType.valueOf(TCBookProperties.getInstance().getString("tcbook.db.type")));

                DataSourceManager dataSourceManager = DataSourceManager.getInstance();
                // initializing db
                dataSourceManager.initDataSource(DATABASE_ALIAS, dsProperties);
            } catch (Exception e) {
                logEx.error("[TCBook] Error initializing database! TCBookMain will not start!", e);
                throw e;
            }

            // starting WEB Server
            webServer = new WebServerManager();

            Thread thread = new Thread(webServer, "WebServerManager");
            thread.start();

            // registering ShutdownManager
            log.info("[TCBookMain] Registering threads on shutdown manager...");
            Runtime.getRuntime().addShutdownHook(new Shutdown());

            Thread.sleep(3000);
            chronometer.stop();
            log.info("[TCBookMain] Application started! Time it took: " + chronometer.getTime() + "ms");
        } catch (Exception e) {
            log.error("[TCBookMain] Error starting TCBookMain! Exception: " + e);
            logEx.error("Error starting TCBookMain ", e);
            System.exit(1);
            throw new RuntimeException(e);
        }
    }

    /**
     * load system properties
     *
     * @throws Exception
     */
    private static void loadSystemProperties() throws Exception {
        String configFile = "config/tcbook.properties";
        tcBookProperties = TCBookProperties.getInstance();
        tcBookProperties.loadProperties(configFile);
    }

    /**
     * load Logback configuration
     *
     * @throws Exception
     */
    private static void loadSystemLog() throws Exception {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        // print logback's internal status
        StatusPrinter.print(lc);

        log = LoggerFactory.getLogger(TCBookMain.class);
        logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);
    }

    /**
     * @return the webServer
     */
    public static WebServerManager getWebServer() {
        return webServer;
    }

}
