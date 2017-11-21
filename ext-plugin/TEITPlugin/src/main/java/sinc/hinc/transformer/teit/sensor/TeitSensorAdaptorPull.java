/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.teit.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import sinc.hinc.abstraction.ResourceDriver.utils.FilesScanner;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

/**
 *
 * @author hungld
 */
public class TeitSensorAdaptorPull implements ProviderQueryAdaptor<Properties> {

    @Override
    public Collection<Properties> getItems(Map<String, String> settings) {
        String workingDir = settings.get("workingdir");
        System.out.println("Settings of TEIT sensor adaptor: " + settings);
        System.out.println("Working dir is: " + workingDir);
        Map<String, String> fileScannerSettings = new HashMap<>();
        fileScannerSettings.put("path", workingDir);
        fileScannerSettings.put("filter", "sensor.conf");
        Map<String, String> metas = FilesScanner.getItems(fileScannerSettings);

        Collection<Properties> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (Entry<String, String> entry : metas.entrySet()) {
                Properties prop = new Properties();
                StringReader reader = new StringReader(entry.getValue());
                prop.load(reader);
                prop.put("executionscript", entry.getKey().replace("sensor.conf", "sensor.sh"));
                result.add(prop);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Cannot read file, the adaptor just pass");
        }
        return result;
    }

    @Override
    public void sendControl(String controlAction, Map<String, String> parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return "teit";
    }

    @Override
    public ResourcesProvider getProviderAPI(Map<String, String> settings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
