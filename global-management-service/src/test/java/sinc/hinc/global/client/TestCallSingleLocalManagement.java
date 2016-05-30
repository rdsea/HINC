/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.client;

import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import java.util.List;
import sinc.hinc.common.API.HINCManagementAPI;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.global.API.ResourcesManagementAPIImpl;
import sinc.hinc.global.API.HINCManagementImpl;
import sinc.hinc.model.API.ResourcesManagementAPI;

/**
 *
 * @author hungld
 */
public class TestCallSingleLocalManagement {

    public static void main(String[] args) throws Exception {

        ResourcesManagementAPI api = new ResourcesManagementAPIImpl();
        HINCManagementAPI mngAPI = new HINCManagementImpl();
        mngAPI.setHINCGlobalMeta(new HINCGlobalMeta("default", "amqp://localhost", "amqp"));

        List<SoftwareDefinedGateway> list = api.querySoftwareDefinedGateways(5000, "");
        System.out.println("list of gateway has: " + list.size() + " items");
        for (SoftwareDefinedGateway gw : list) {
            System.out.println(gw.toJson());
            System.out.println("==============");
        }
    }
}
