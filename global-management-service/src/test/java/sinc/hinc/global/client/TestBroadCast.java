/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*

package sinc.hinc.global.client;

import sinc.hinc.common.API.HINCManagementAPI;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.global.API.HINCManagementImpl;
import sinc.hinc.global.API.ResourcesManagementAPIImpl;
import sinc.hinc.model.API.ResourcesManagementAPI;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;
import java.util.List;
import java.util.Set;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

*/
/**
 * @author hungld
 *//*

public class TestBroadCast {

    public static void main(String[] args) throws Exception {
        AbstractDAO<HincLocalMeta> metaDao = new AbstractDAO<>(HincLocalMeta.class);

        ResourcesManagementAPI api = new ResourcesManagementAPIImpl();
        HINCManagementAPI mngAPI = new HINCManagementImpl();
        mngAPI.setHINCGlobalMeta(new HINCGlobalMeta("default", "amqp://localhost", "amqp"));
        mngAPI.queryHINCLocal(3000);

        System.out.println("NOW READ AGAIN ............................");
        List<HincLocalMeta> metas = metaDao.readAll();
        if (metas == null) {
            System.out.println("No HINC Local is saved !");
        } else {
            System.out.println("Number of local services: " + metas.size());
            for (HincLocalMeta meta : metas) {
                System.out.println(meta.toJson());
            }
        }
//
        Set<IoTUnit> gateways = api.queryIoTUnits(20000, null, null, 0, "false");
        System.out.println("Gateway number: " + gateways.size());

        AbstractDAO<IoTUnit> gwDAO = new AbstractDAO<>(IoTUnit.class);
        for (IoTUnit gw : gwDAO.readAll()) {
            System.out.println("Gateway UUID: " + gw.getUuid());
        }

        AbstractDAO<DataPoint> dpDAO = new AbstractDAO<>(DataPoint.class);
        for (DataPoint dp : dpDAO.readAll()) {
            System.out.println("DataPoint UUID: " + dp.getDatatype());
        }

//        List<VNF> routers = client.queryVNF_Multicast();
//        System.out.println("Router number: " + routers.size());
//        NetworkGraphGenerator generator = new NetworkGraphGenerator();
//        String graph = generator.generateGraph(gateways, routers);
//        System.out.println(graph);
    }

}
*/
