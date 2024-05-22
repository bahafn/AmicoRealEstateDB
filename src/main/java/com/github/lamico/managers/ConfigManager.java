package com.github.lamico.managers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A utility class for managing configuration properties.
 */
public class ConfigManager {

    /**
     * The path to the configuration file.
     */
    private static final String CONFIG_FILE_PATH = "config.properties";

    /**
     * The properties object that stores the configuration properties.
     */
    private static final Properties properties = new Properties();

    // Load configuration properties from file
    static {
        try {
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
        }
    }

    /**
     * Returns the value of the specified property as a string.
     * 
     * @param key the key of the property
     * @return the value of the property, or null if the property does not exist
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Returns the value of the specified property as an integer.
     * 
     * @param key the key of the property
     * @return the value of the property as an integer, or -1 if the property does not exist or is not a valid integer
     */
    public static int getIntProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            return -1;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Returns the value of the specified property as a boolean.
     * 
     * @param key the key of the property
     * @return the value of the property as a boolean
     */
    public static boolean getBooleanProperty(String key) {
        String value = properties.getProperty(key);
        return Boolean.parseBoolean(value);
    }

    /**
     * Returns the value of the specified property as a double.
     * 
     * @param key the key of the property
     * @return the value of the property as a double, or -1 if the property does not exist or is not a valid double
     */
    public static double getDoubleProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            return -1;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Sets the value of the specified property.
     * 
     * @param key the key of the property
     * @param value the value of the property
     */
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        savePropertiesToFile();
    }

    /**
     * Sets the value of the specified property as an integer.
     * 
     * @param key the key of the property
     * @param value the value of the property
     */
    public static void setIntProperty(String key, int value) {
        properties.setProperty(key, String.valueOf(value));
        savePropertiesToFile();
    }

    /**
     * Sets the value of the specified property as a boolean.
     * 
     * @param key the key of the property
     * @param value the value of the property
     */
    public static void setBooleanProperty(String key, boolean value) {
        properties.setProperty(key, String.valueOf(value));
        savePropertiesToFile();
    }

    /**
     * Sets the value of the specified property as a double.
     * 
     * @param key the key of the property
     * @param value the value of the property
     */
    public static void setDoubleProperty(String key, double value) {
        properties.setProperty(key, String.valueOf(value));
        savePropertiesToFile();
    }

    /**
     * Saves the configuration properties to the file.
     */
    private static void savePropertiesToFile() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(CONFIG_FILE_PATH)) {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
        }
    }

    /**
     * A test method for the ConfigManager class.
     * 
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        System.out.println(getProperty("Username"));
    }
}