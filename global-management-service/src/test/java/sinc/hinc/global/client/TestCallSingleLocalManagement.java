/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.client;

import sinc.hinc.global.management.CommunicationManager;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import java.util.List;

/**
 *
 * @author hungld
 */
public class TestCallSingleLocalManagement {

    public static void main(String[] args) throws Exception {
    	
        CommunicationManager client = new CommunicationManager("test", "amqp://localhost","amqp");

        List<SoftwareDefinedGateway> list = client.querySoftwareDefinedGateway_Broadcast(5000);
        System.out.println("list of gateway has: " + list.size() + " items");
        for (SoftwareDefinedGateway gw : list) {            
            System.out.println(gw.toJson());
            System.out.println("==============");
        }
    }
}
