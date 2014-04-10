package com.tcbook.ws.util;

import java.util.Properties;

public class TCBookProperties {

	private static SmartProperties defaultInstance = new SmartProperties(new Properties());

	private TCBookProperties() {
	}

	public static SmartProperties getInstance() {
		return defaultInstance;
	}

}
