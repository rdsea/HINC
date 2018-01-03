/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.testrigprovider.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

/**
 *
 * @author hungld
 */
public class TestRigProviderAdaptor implements ProviderQueryAdaptor<TestRigMetadataItem> {

    @Override
    public Collection<TestRigMetadataItem> getItems(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();
        String username = settings.get("username");
        String password = settings.get("password");
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        try {
            String dataJson = RestHandler.build(endpoint + "/rig",username,password).callGet();
            if (dataJson ==null)
                return null;
            ObjectMapper mapper = new ObjectMapper();
            
            RigDeviceData data = mapper.readValue(dataJson, RigDeviceData.class);
            return data.getDevices();
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
        return "testrig";
    }

    @Override
    public ResourcesProvider getProviderAPI(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();
        String provider_ref = endpoint + "/sut";
        ResourcesProvider rp = new ResourcesProvider("TestRig", endpoint, settings);
        rp.hasApi(new ControlPoint("shutdown", ControlPoint.InvokeProtocol.GET, endpoint + "/reboot_system", ControlPoint.ControlType.SELF_CONFIGURE).belongTo(provider_ref));
        return rp;
    }

}
