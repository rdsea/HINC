/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*

package sinc.hinc.global.client;

import sinc.hinc.common.API.HINCManagementAPI;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.global.API.HINCManagementImpl;
import sinc.hinc.global.API.ResourcesManagementAPIImpl;
import sinc.hinc.model.API.ResourcesManagementAPI;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;

import java.util.Set;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

*/
/**
 * @author hungld
 *//*

public class TestCallSingleLocalManagement {

    public static void main(String[] args) throws Exception {

        ResourcesManagementAPI api = new ResourcesManagementAPIImpl();
        HINCManagementAPI mngAPI = new HINCManagementImpl();
        mngAPI.setHINCGlobalMeta(new HINCGlobalMeta("default", "amqp://localhost", "amqp"));

        Set<IoTUnit> list = api.queryIoTUnits(5000, "", null, 0, "false");
        System.out.println("list of gateway has: " + list.size() + " items");
        for (IoTUnit unit : list) {
            System.out.println(unit.toJson());
            System.out.println("==============");
        }
    }
}
*/
