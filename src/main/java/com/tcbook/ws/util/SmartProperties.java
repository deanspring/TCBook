package com.tcbook.ws.util;

import java.io.*;
import java.util.Properties;

public class SmartProperties {

    private Properties properties;

    public SmartProperties(Properties properties) {
        this.properties = properties;
    }

    public String getString(String property, String defaultValue) {
        String value = properties.getProperty(property);
        if (value == null)
            return defaultValue;
        return value;
    }

    public Integer getInt(String property, Integer defaultValue) {
        String value = properties.getProperty(property);
        if (value == null)
            return defaultValue;

        try {
            Integer result = new Integer(value);
            return result;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Long getLong(String property, Long defaultValue) {
        String value = properties.getProperty(property);
        if (value == null)
            return defaultValue;

        try {
            Long result = new Long(value);
            return result;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Boolean getBoolean(String property, Boolean defaultValue) {
        String value = properties.getProperty(property);
        if (value == null)
            return defaultValue;

        try {
            Boolean result = Boolean.valueOf(value);
            return result;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Double getDouble(String property, Double defaultValue) {
        String value = properties.getProperty(property);
        if (value == null)
            return defaultValue;

        try {
            Double result = new Double(value);
            return result;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public void loadProperties(String filename) throws IOException {
        File file = new File(filename);
        FileInputStream is = new FileInputStream(file);
        properties.load(is);
        is.close();
    }

    public void loadProperties(InputStream inputStream) throws IOException {
        properties.clear();

        properties.load(inputStream);
        inputStream.close();
    }

    public void loadProperties(Properties p) throws IOException {
        if (p == null)
            throw new IllegalArgumentException();

        properties.putAll(p);
    }

    public String getString(String s) {
        String result = getString(s, null);
        if (result == null)
            throw new RuntimeException("Missing value for mandatory property " + s);
        return result;
    }

    public int getInt(String s) {
        Integer result = getInt(s, null);
        if (result == null)
            throw new RuntimeException("Missing value for mandatory property " + s);
        return result;
    }

    public boolean getBoolean(String s) {
        Boolean result = getBoolean(s, null);
        if (result == null)
            throw new RuntimeException("Missing value for mandatory property " + s);
        return result;
    }

    public double getDouble(String s) {
        Double result = getDouble(s, null);
        if (result == null)
            throw new RuntimeException("Missing value for mandatory property " + s);
        return result;
    }

    public Properties getUnderlyingProperties() {
        return properties;
    }

    public void setUnderlyingProperties(Properties p) {
        properties = p;
    }

}
