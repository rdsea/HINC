/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.client;

import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import java.util.List;

/**
 *
 * @author hungld
 */
public class TestCallSingleLocalManagement {

    public static void main(String[] args) throws Exception {
        QueryManager client = new QueryManager("test", "amqp://128.130.172.215", "amqp");

//        SoftwareDefinedGateway gw = client.querySoftwareDefinedGateway_Unicast("9945aa57-54fb-4a21-9a04-1230c9c288ee");
        List<SoftwareDefinedGateway> list = client.querySoftwareDefinedGateway_Broadcast(15000);
        for (SoftwareDefinedGateway gw : list) {
            gw.toJson();
            System.out.println("===============");
        }
    }
}
