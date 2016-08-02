/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.mock;

import sinc.hinc.model.VirtualComputingResource.Capability;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint.InvokeProtocol;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.model.VirtualNetworkResource.NetworkService;
import sinc.hinc.model.VirtualNetworkResource.VNF;
import sinc.hinc.model.VirtualNetworkResource.VNFForwardGraph;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;

/**
 *
 * @author hungld
 */
public class MockIoT {

    public static void main(String[] arg) throws Exception {
        SoftwareDefinedGateway gateway = new SoftwareDefinedGateway();

        Capability control1 = new ControlPoint("sensor1", "changeSensorRate", "change data rate", InvokeProtocol.POST, "http://localhost:8080/salsa-engine/rest/services/IoTSensors/nodes/SensorUnit/instances/1/action_queue/changeRate/parameters/{1}");
        Capability control2 = new ControlPoint("sensor1", "setProtocolMQTT", "change to MQTT mode", InvokeProtocol.POST, "http://localhost:8080/salsa-engine/rest/services/IoTSensors/nodes/SensorUnit/instances/1/action_queue/setProtocolMQTT");
        Capability control3 = new ControlPoint("sensor1", "setProtocolDRY", "change DRY mode", InvokeProtocol.POST, "http://localhost:8080/salsa-engine/rest/services/IoTSensors/nodes/SensorUnit/instances/1/action_queue/setProtocolDRY");
        gateway.hasCapabilities(Arrays.asList(control1, control2, control3));

        Capability data1 = new DataPoint("sensor1", "temperature1", "temperature of room1", "temperature", "C");
        Capability data2 = new DataPoint("sensor1", "humidity1", "humidity of room1", "humidity", "%");
        gateway.hasCapability(data1);
        gateway.hasCapability(data2);

        gateway.getMeta().put("model", "G2021");
        gateway.getMeta().put("owner", "tuwien");
        gateway.getMeta().put("location", "building1");
        gateway.setName("Gateway1");
        gateway.setUuid("9321a1c2-a622-4b4c-ba3d-f51e8af79460");

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(gateway));

        // network
//        NetworkService network = new NetworkService();        
//        
//        network.getAccessPoints().add(new AccessPoint("172.17.0.150"));
//        network.getAccessPoints().add(new AccessPoint("172.17.0.152"));
//
//        VNF vnf1 = new VNF("weave-router1", "IGRP", new AccessPoint("172.17.0.150"));
//        VNF vnf2 = new VNF("weave-router2", "IGRP", new AccessPoint("172.17.0.151"));
//        VNF vnf3 = new VNF("weave-router3", "IGRP", new AccessPoint("172.17.0.152"));
//        VNFForwardGraph graph = new VNFForwardGraph();
//        graph.setNodes(Arrays.asList(vnf1, vnf2, vnf3));
//        graph.getLinks().add(new VNFForwardGraph.VNFLink("weave-router1", "weave-router2", "100Mbps"));
//        graph.getLinks().add(new VNFForwardGraph.VNFLink("weave-router2", "weave-router3", "500Mbps"));
//
//        network.setVnfForwardGraphs(Arrays.asList(graph));
        //System.out.println(mapper.writeValueAsString(gateway));
//        System.out.println(mapper.writeValueAsString(network));
    }
}
