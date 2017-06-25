/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import sinc.hinc.dummyprovider.provider.DummyData;
import sinc.hinc.dummyprovider.provider.DummyMetadataItem;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

/**
 *
 * @author hungld
 */
public class DummyProviderAdaptor implements ProviderQueryAdaptor<DummyMetadataItem> {

    @Override
    public Collection<DummyMetadataItem> getItems(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        try {
            String dataJson = RestHandler.build(endpoint + "/items").callGet();
            ObjectMapper mapper = new ObjectMapper();
            DummyData data = mapper.readValue(dataJson, DummyData.class);
            return data.getDataItems();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void sendControl(String controlAction, Map<String, String> parameters) {
        // do nothing yet
    }

    @Override
    public String getName() {
        return "dummy";
    }

    @Override
    public ResourcesProvider getProviderAPI(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();
        String provider_ref = endpoint + "/controlapi";
        ResourcesProvider rp = new ResourcesProvider("dummy", endpoint, settings);
        rp.hasApi(new ControlPoint("changepolicy", ControlPoint.InvokeProtocol.POST, endpoint + "/changepolicy", ControlPoint.ControlType.SELF_CONFIGURE).belongTo(provider_ref));
        rp.hasApi(new ControlPoint("health", ControlPoint.InvokeProtocol.GET, endpoint + "/health", ControlPoint.ControlType.SELF_CONFIGURE).belongTo(provider_ref));
        return rp;
    }

}
