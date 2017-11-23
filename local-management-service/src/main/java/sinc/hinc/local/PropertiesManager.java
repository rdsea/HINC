/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;

/**
 *
 * @author Duc-Hung Le
 */
public class PropertiesManager {

    static Logger logger = HincConfiguration.getLogger();

    public static Properties getParameters(String configFile) {
        Properties configuration = new Properties();
        try {
            File f = new File(configFile);
            if (!f.exists()) {
                logger.error("Configuration file not found: " + configFile + ". Return a black properties.");
                return new Properties();
            }
            configuration.load(new FileReader(f));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return configuration;
    }

    public static String getParameter(String key, String configFile) {
        Properties configuration = getParameters(configFile);
        if (configuration != null) {
            System.out.println("Trying to get parameter: " + key + " in the configuration file:" + configFile + ", value: " + configuration.getProperty(key));
            return configuration.getProperty(key);
        }
        System.out.println("Cannot load the configuration file:" + configFile + " to load the key: " + key);

        return null;
    }

    public static void saveParameter(String key, String value, String configFile) {
        Properties configuration = getParameters(configFile);

        System.out.println("Trying to save parameter: " + key + "=" + value + ", in the configuration file:" + configFile);
        try {
            OutputStream output = new FileOutputStream(configFile);
            configuration.store(output, null);
        } catch (FileNotFoundException ex) {
            System.out.println("Cannot find the configFile: " + configFile + ", to store properties ! Error: " + ex);
        } catch (IOException ex) {
            System.out.println("Cannot write to configFile: " + configFile + ", to store properties ! Error: " + ex);
        }
    }

    /**
     * The configuration line in this format:<name>:<key>=<value>
     * This function get the key/value and put into the map
     *
     * @param configFile
     * @param name
     * @return
     */
    public static Map<String, String> getSettings(String name, String configFile) {
        logger.debug("Loading settings for: {} in file {}", name, configFile);
        Map<String, String> map = new HashMap<>();
        Properties props = getParameters(configFile);
        if (props != null) {
            for (Entry<Object, Object> entry : props.entrySet()) {
                if (entry.getKey().toString().contains(".")) {
                    String entryName = entry.getKey().toString().split("\\.")[0].trim();
                    if (entryName.equals(name.trim())) {
                        String entryKey = entry.getKey().toString().split("\\.")[1].trim();
                        String entryValue = entry.getValue().toString().trim();
                        map.put(entryKey, entryValue);
                    }
                } else {
                    logger.warn("A line in configuration file is wrong in format: {}. Should be name.key=value", entry.toString());
                }
            }
        } else {
            logger.error("Cannot load the config file: {}", configFile);
        }
        logger.debug("Settings loaded: {}", map.toString());
        return map;
    }


}