/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.client;

import sinc.hinc.global.client.Algorithms;
import sinc.hinc.global.client.QueryManager;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.VNF;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hungld
 */
public class ChangeNetworkFunction {

    public static void main(String[] arg) {
        QueryManager client = new QueryManager("myClient", "amqp://128.130.172.215", "amqp");
        client.getListOfDelise();

        List<VNF> vnfs = client.queryVNF_Multicast();
        List<SoftwareDefinedGateway> gws = client.querySoftwareDefinedGateway_Multicast();

        Map<SoftwareDefinedGateway, VNF> linkMap = Algorithms.reconfigureNetworks(gws, vnfs);
//        for (SoftwareDefinedGateway gw : linkMap.keySet()) {
//            client.redirectGateway(gw, linkMap.get(gw));
//        }
    }
}
