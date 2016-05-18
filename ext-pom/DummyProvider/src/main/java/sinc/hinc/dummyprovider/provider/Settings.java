/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author hungld
 */
public class Settings {

    // the number of data point item
    static int dataPointsMin;
    static int dataPointsMax;
    // the amount of mili-second when a new update cycle come
    static int updateCycleMin;
    static int updateCycleMax;
    // a number of datapoints are removed/added/update in each cycle.
    // note that the piority are: update->remove->add. That mean the one
    // which is updated will not be removed, and the remove will not belog the dataPointMin, add not be over dataPointMax
    static int removeMin;
    static int removeMax;
    static int appearMin;
    static int appearMax;
    static int updateMin;
    static int updateMax;

    {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            System.out.println(prop.getProperty("database"));
            System.out.println(prop.getProperty("dbuser"));
            System.out.println(prop.getProperty("dbpassword"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /**
         * By default, there will be 10 to 20 instance at a time Every 2 to 5 second, a changing happen Possibly maximum 1 updated, 1 removed and 1 appear
         */
        dataPointsMin = getValueOrDefault(prop.getProperty("dataPointMin"), 10);
        dataPointsMax = getValueOrDefault(prop.getProperty("dataPointMax"), 20);
        updateCycleMin = getValueOrDefault(prop.getProperty("updateCycleMin"), 2000);
        updateCycleMax = getValueOrDefault(prop.getProperty("updateCycleMax"), 5000);
        removeMin = getValueOrDefault(prop.getProperty("removeMin"), 0);
        removeMax = getValueOrDefault(prop.getProperty("removeMax"), 1);
        appearMin = getValueOrDefault(prop.getProperty("appearMin"), 0);
        appearMax = getValueOrDefault(prop.getProperty("appearMax"), 1);
        updateMin = getValueOrDefault(prop.getProperty("updateMin"), 0);
        updateMax = getValueOrDefault(prop.getProperty("updateMax"), 1);

        // just a quick validate        
    }

    static int getValueOrDefault(String valueStr, int defaultVal) {
        if (valueStr == null || valueStr.isEmpty()) {
            return defaultVal;
        } else {
            try {
                return Integer.valueOf(valueStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return defaultVal;
            }
        }
    }

    public static int getDataPointsMin() {
        return dataPointsMin;
    }

    public static void setDataPointsMin(int dataPointsMin) {
        Settings.dataPointsMin = dataPointsMin;
    }

    public static int getDataPointsMax() {
        return dataPointsMax;
    }

    public static void setDataPointsMax(int dataPointsMax) {
        Settings.dataPointsMax = dataPointsMax;
    }

    public static int getUpdateCycleMin() {
        return updateCycleMin;
    }

    public static void setUpdateCycleMin(int updateCycleMin) {
        Settings.updateCycleMin = updateCycleMin;
    }

    public static int getUpdateCycleMax() {
        return updateCycleMax;
    }

    public static void setUpdateCycleMax(int updateCycleMax) {
        Settings.updateCycleMax = updateCycleMax;
    }

    public static int getRemoveMin() {
        return removeMin;
    }

    public static void setRemoveMin(int removeMin) {
        Settings.removeMin = removeMin;
    }

    public static int getRemoveMax() {
        return removeMax;
    }

    public static void setRemoveMax(int removeMax) {
        Settings.removeMax = removeMax;
    }

    public static int getAppearMin() {
        return appearMin;
    }

    public static void setAppearMin(int appearMin) {
        Settings.appearMin = appearMin;
    }

    public static int getAppearMax() {
        return appearMax;
    }

    public static void setAppearMax(int appearMax) {
        Settings.appearMax = appearMax;
    }

    public static int getUpdateMin() {
        return updateMin;
    }

    public static void setUpdateMin(int updateMin) {
        Settings.updateMin = updateMin;
    }

    public static int getUpdateMax() {
        return updateMax;
    }

    public static void setUpdateMax(int updateMax) {
        Settings.updateMax = updateMax;
    }

}
