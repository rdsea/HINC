/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.opendaylightplugin;

import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import java.util.Collection;
import java.util.Map;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;

/**
 *
 * @author hungld
 */
public class OpenDaylightadaptor implements ProviderQueryAdaptor<InventoryData> {

    String baseURL = "http://100.96.9.107:8181";

    String confUrl = baseURL + "/restconf/config/";
    String operUrl = baseURL + "/restconf/operational/";

    String findNodes = operUrl + "/opendaylight-inventory:nodes/";
    String findTopo = operUrl + "/network-topology:network-topology/";
    String findNodeConnector = operUrl + "/opendaylight-inventory:nodes/node/node-connector/";
    String findTopology = operUrl + "/network-topology:network-topology/topology/flow:1/";
    String findFlow = confUrl + "/opendaylight-inventory:nodes/node/openflow:1/table/0/";

    String inventoryURL = "/restconf/operational/opendaylight-inventory:nodes";

    public static void main(String[] args) throws IOException {
        //GET http://100.96.9.107:8181/restconf/operational/opendaylight-inventory:nodes
        OpenDaylightadaptor odl = new OpenDaylightadaptor();
        
        String authStr = "admin:admin";
        String encodedAuthStr = Base64.encodeBase64String(authStr.getBytes());
        System.out.println(encodedAuthStr);
//        System.out.println("Start calling....");
//        String result = RestHandler.build(odl.findTopo).contentType("application/json").accept("application/json").header("Authorization", "Basic " + encodedAuthStr).callGet();
//        System.out.println("Result: " + result);
    }

    @Override
    public Collection<InventoryData> getItems(Map<String, String> settings) {
        return null;
    }

    @Override
    public void sendControl(String controlAction, Map<String, String> parameters) {
        // TODO: implement this 
    }

    @Override
    public String getName() {
        return "opendaylight";
    }

}
