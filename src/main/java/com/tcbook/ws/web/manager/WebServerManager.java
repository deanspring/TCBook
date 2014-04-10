package com.tcbook.ws.web.manager;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.tcbook.ws.util.TCBookConstants;
import com.tcbook.ws.util.TCBookProperties;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerManager implements Runnable {

	private static Logger log = LoggerFactory.getLogger(WebServerManager.class);
	private static Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

	private Server webServer;

	public WebServerManager() {
	}

	@Override
	public void run() {
		log.info("[WEB_SERVER_MANAGER] Starting server for WebServices...");

		try {
			webServer = startServer();
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}

		} catch (Exception e) {
			log.error("[WEB_SERVER_MANAGER] Exception on Jersey init!", e);
			System.exit(1);
		}
	}

	private Server startServer() throws Exception {
		Server server = null;

		try {
			server = new Server();

			Connector connector = configureConnector(TCBookProperties.getInstance().getInt("http.server.port"));
			server.addConnector(connector);

			ContextHandlerCollection contexts = new ContextHandlerCollection();

			contexts.addHandler(getServletHandler(connector));
			contexts.addHandler(getResourceHandler());

			server.setHandler(contexts);
			server.start();

			log.info("[WEB_SERVER_MANAGER] WebServer started!");
		} catch (Exception e) {
			log.error("[WEB_SERVER_MANAGER] Error starting Jetty webserver: " + e.toString());
			logEx.error("[WEB_SERVER_MANAGER] Error starting Jetty webserver ", e);
			System.exit(1);
		}

		return server;
	}

	protected ServletContextHandler getServletHandler(Connector connector) {
		ServletHolder tcbookServletHolder = new ServletHolder(ServletContainer.class);
		tcbookServletHolder.setInitParameter("com.sun.jersey.config.property.packages", "com.tcbook.ws.web.rest");

		ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContext.addServlet(tcbookServletHolder, "/*");
		servletContext.setContextPath("/tcbook/s");

		servletContext.setConnectorNames(new String[] {connector.getName()});

		return servletContext;
	}

	protected ContextHandler getResourceHandler() {
		ContextHandler contextHandler = new ContextHandler();
		contextHandler.setContextPath("/tcbook");
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setResourceBase("./src/main/webapp");
		resourceHandler.setDirectoriesListed(false);
		contextHandler.setHandler(resourceHandler);

		return contextHandler;
	}

	private SelectChannelConnector configureConnector(int port) {
		log.info("[WEB_SERVER_MANAGER] Starting WebServer[" + TCBookProperties.getInstance().getString("http.server.name", "jetty") + "] on port " + port + "...");

		SelectChannelConnector connector = new SelectChannelConnector();

		connector.setPort(port);
		connector.setName("port" + String.valueOf(port));

		int maxIdleTime = TCBookProperties.getInstance().getInt("http.server.maxIdleTime", 30000);
		connector.setMaxIdleTime(maxIdleTime);

		int threadPoolNumber = TCBookProperties.getInstance().getInt("http.server.threadPool", 4);
		int minThreads = TCBookProperties.getInstance().getInt("http.server.threadPool.minThreads", 10);
		int maxThreads = TCBookProperties.getInstance().getInt("http.server.threadPool.maxThreads", 100);

		QueuedThreadPool threadPool = new QueuedThreadPool(threadPoolNumber);
		threadPool.setMinThreads(minThreads);
		threadPool.setMaxThreads(maxThreads);
		connector.setThreadPool(threadPool);

		int acceptQueueSize = TCBookProperties.getInstance().getInt("http.server.acceptQueueSize", 1000);
		connector.setAcceptQueueSize(acceptQueueSize);

		int acceptorSize = TCBookProperties.getInstance().getInt("http.server.acceptorsSize", 16);

		connector.setAcceptors(acceptorSize);

		return connector;
	}

	public void shutdown() {
		log.info("[WEB_SERVER_MANAGER] Stopping Webserver...");

		try {
			webServer.stop();
			log.info("[WEB_SERVER_MANAGER] Webserver stopped.");
		} catch (Exception e) {
			log.error("[WEB_SERVER_MANAGER] Error on stop WebServer!", e);
			System.exit(1);
		}

		synchronized (this) {
			this.notifyAll();
		}
	}
}
