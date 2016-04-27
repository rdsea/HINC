/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openiot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;
import sinc.hinc.abstraction.ResourceDriver.utils.RESTHandler;
import sinc.hinc.transformer.openiot.model.OpenIoTSensorWrapper;

/**
 *
 * @author hungld
 */
public class OpenIoTAdaptor implements ProviderAdaptor<OpenIoTSensorWrapper> {

    @Override
    public Collection<OpenIoTSensorWrapper> getItems(Map<String, String> settings) {
        String endpoint = settings.get("endpoint");
        String json = RESTHandler.callRest(endpoint, RESTHandler.HttpVerb.GET, null, null, "*/*");
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<OpenIoTSensorWrapper> data = mapper.readValue(json, new TypeReference<List<OpenIoTSensorWrapper>>() {
            });
            return data;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void sendControl(String controlAction, Map<String, String> parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
