package com.saucedemo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to read configuration properties from config.properties file.
 */
public class ConfigReader {

    private static Properties properties;

    static {
        try {
            properties = new Properties();
            // Try loading from classpath first
            InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties");
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                // Fallback to direct file path
                FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
                properties.load(fileInputStream);
            }
        } catch (IOException e) {
            System.err.println("Failed to load config.properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return properties.getProperty("baseUrl", "https://www.saucedemo.com/");
    }

    public static String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicitWait", "10"));
    }

    public static String getStandardUser() {
        return properties.getProperty("standardUser", "standard_user");
    }

    public static String getLockedOutUser() {
        return properties.getProperty("lockedOutUser", "locked_out_user");
    }

    public static String getDefaultPassword() {
        return properties.getProperty("defaultPassword", "secret_sauce");
    }
}
