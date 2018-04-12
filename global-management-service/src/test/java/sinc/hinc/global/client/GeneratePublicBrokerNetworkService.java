/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*

package sinc.hinc.global.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import sinc.hinc.model.API.ResourcesManagementAPI;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;

*/
/**
 *
 * @author hungld
 *//*

public class GeneratePublicBrokerNetworkService {

    public static void main(String[] args) {
        List<NetworkFunctionService> nws = getMockNetworkService();
        ResourcesManagementAPI rest = (ResourcesManagementAPI) JAXRSClientFactory.create("http://localhost:8080/global-management-service-1.0/rest", ResourcesManagementAPI.class, Collections.singletonList(new JacksonJaxbJsonProvider()));
        for (NetworkFunctionService s : nws) {
            System.out.println("Adding network service: " + s.getAccessPoint().getEndpoint());
            rest.addNetworkService(s);
        }
    }

    public static List<NetworkFunctionService> getMockNetworkService() {
        List<NetworkFunctionService> nws = new ArrayList<>();
        nws.add(new NetworkFunctionService(UUID.randomUUID().toString(), "iot.eclipse.org", NetworkFunctionService.NetworkServiceType.BROKER_MQTT, new AccessPoint("tcp://iot.eclipse.org:1883")));
        nws.add(new NetworkFunctionService(UUID.randomUUID().toString(), "test.mosquitto.org", NetworkFunctionService.NetworkServiceType.BROKER_MQTT, new AccessPoint("tcp://test.mosquitto.org:1883")));
        nws.add(new NetworkFunctionService(UUID.randomUUID().toString(), "broker.hivemq.com", NetworkFunctionService.NetworkServiceType.BROKER_MQTT, new AccessPoint("tcp://broker.hivemq.com:1883")));
        nws.add(new NetworkFunctionService(UUID.randomUUID().toString(), "californium.eclipse.org", NetworkFunctionService.NetworkServiceType.BROKER_COAP, new AccessPoint("coap://californium.eclipse.org:5683/")));
        return nws;
    }
}
*/
