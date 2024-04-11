package org.dataProviders;

import java.util.Properties;

public final class PropertiesStorage {

    private final static String jsonsPath = "jsonsPath";


    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    private Properties properties;

    private static volatile PropertiesStorage instance;

    private PropertiesStorage() {
    }
    public static PropertiesStorage getInstance() {
        PropertiesStorage result = instance;

        if(result != null) {
            return result;
        }

        synchronized (PropertiesStorage.class) {
            if(instance == null) {
                instance = new PropertiesStorage();
            }
            return instance;
        }
    }

    public String getJsonsPath() {
        return getValueOrNull(jsonsPath);
    }

    private String getValueOrNull(String key) {
        if(properties == null || !properties.containsKey(key)) {
            return null;
        }
        return properties.getProperty(key);
    }
}
