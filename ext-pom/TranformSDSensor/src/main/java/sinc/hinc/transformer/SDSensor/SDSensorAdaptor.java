/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.SDSensor;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;
import sinc.hinc.abstraction.ResourceDriver.utils.FilesScanner;

/**
 *
 * @author hungld
 */
public class SDSensorAdaptor implements ProviderAdaptor<SDSensorMeta> {

    @Override
    public Collection<SDSensorMeta> getItems(Map<String, String> settings) {
        String sensorMetaFilePath = settings.get("path");
        Collection<String> loadedItems = FilesScanner.getItems(settings);
        Collection<SDSensorMeta> result = new ArrayList<>();

        for (String rawData : loadedItems) {
            Properties p = new Properties();
            StringReader reader = new StringReader(rawData);
            String baseDir = sensorMetaFilePath.substring(0, sensorMetaFilePath.lastIndexOf("/"));
            try {
                p.load(reader);
                SDSensorMeta meta = new SDSensorMeta(p.getProperty("name"), p.getProperty("type"), p.getProperty("rate"), p.getProperty("protocol"));
                for (Object keyObj : p.keySet()) {
                    String key = (String) keyObj;
                    if (key.startsWith("action.")) {
                        String actionName = key.substring(key.indexOf(".") + 1);
                        meta.getActions().put(actionName, baseDir + "/" + p.getProperty(key));
                    }
                }
                result.add(meta);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void sendControl(String controlAction, Map<String, String> parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getName() {
        return "sdsensor";
    }

}
