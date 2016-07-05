/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.teit;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;
import sinc.hinc.abstraction.ResourceDriver.utils.FilesScanner;

/**
 *
 * @author hungld
 */
public class TeitSensorAdaptor implements ProviderAdaptor<DummyMetadataItem> {

    @Override
    public Collection<DummyMetadataItem> getItems(Map<String, String> settings) {
        String workingDir = settings.get("workingDir");
        System.out.println("Settings of TEIT sensor adaptor: " + settings);
        System.out.println("Working dir: " + workingDir);
        Map<String, String> fileScannerSettings = new HashMap<>();
        fileScannerSettings.put("path", workingDir);
        fileScannerSettings.put("filter", ".meta");
        Collection<String> metas = FilesScanner.getItems(fileScannerSettings);
        
        Collection<DummyMetadataItem> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (String metaString : metas) {
                DummyMetadataItem item = mapper.readValue(metaString, DummyMetadataItem.class);              
                result.add(item);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
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
    
    
}
