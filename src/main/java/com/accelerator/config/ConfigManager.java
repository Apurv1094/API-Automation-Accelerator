package com.accelerator.config;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {
    private ConfigManager() {
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value != null && !value.isBlank())
            return value.trim();
        String envKey = key.toUpperCase().replace('.', '_').replace('-', '_');
        value = System.getenv(envKey);
        if (value != null && !value.isBlank())
            return value.trim();
        String environment = System.getProperty("env", "local");
        if (!environment.matches("[a-zA-Z0-9_-]+"))
            throw new IllegalArgumentException("Invalid env name");
        Properties props = new Properties();
        try (InputStream in = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config/" + environment + ".properties")) {
            if (in == null)
                throw new IllegalStateException("Missing config/" + environment + ".properties");
            props.load(in);
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Cannot load environment config", e);
        }
        return props.getProperty(key, "").trim();
    }

    public static String getOrDefault(String key, String fallback) {
        String value = get(key);
        return value.isBlank() ? fallback : value;
    }

    public static String require(String key) {
        String value = get(key);
        if (value.isBlank())
            throw new IllegalStateException("Missing required configuration: " + key);
        return value;
    }
}
