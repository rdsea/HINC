/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.client;

import at.ac.tuwien.dsg.hinc.cache.CacheGateway;
import at.ac.tuwien.dsg.hinc.cache.CacheVNF;
import at.ac.tuwien.dsg.hinc.client.RelationshipManagement.NetworkGraphGenerator;
import at.ac.tuwien.dsg.hinc.communication.messagePayloads.HincMeta;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import at.ac.tuwien.dsg.hinc.model.VirtualNetworkResource.VNF;
import at.ac.tuwien.dsg.hinc.repository.DAO.orientDB.AbstractDAO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hungld
 */
public class TestBroadCast {
    
   

    public static void main(String[] args) throws Exception {
        AbstractDAO<HincMeta> metaDao = new AbstractDAO<>(HincMeta.class);

        QueryManager client = new QueryManager("hung", "amqp://128.130.172.215", "amqp");
        client.synDelise(3000);

        List<HincMeta> metas = metaDao.readAll();
        System.out.println("Number of local services: " + metas.size());
        for (HincMeta meta : metas) {
            System.out.println(meta.toJson());
        }

        List<SoftwareDefinedGateway> gateways = client.querySoftwareDefinedGateway_Multicast();
        System.out.println("Gateway number: " + gateways.size());
        
        AbstractDAO<SoftwareDefinedGateway> gwDAO = new AbstractDAO<>(SoftwareDefinedGateway.class);
        for (SoftwareDefinedGateway gw: gwDAO.readAll()){
            System.out.println("Gateway UUID: " + gw.getUuid());
        }
                
        AbstractDAO<DataPoint> dpDAO = new AbstractDAO<>(DataPoint.class);
        for (DataPoint dp: dpDAO.readAll()){
            System.out.println("DataPoint UUID: " + dp.getUuid());
        }

//        List<VNF> routers = client.queryVNF_Multicast();
//        System.out.println("Router number: " + routers.size());
//        NetworkGraphGenerator generator = new NetworkGraphGenerator();
//        String graph = generator.generateGraph(gateways, routers);
//        System.out.println(graph);
    }

    public static void main1(String[] args) throws Exception {
        QueryManager client = new QueryManager("hung", "amqp://128.130.172.215", "amqp");
        client.synDelise(3000);

//        List<SoftwareDefinedGateway> gateways = client.querySoftwareDefinedGatewayBroadcast();
        List<SoftwareDefinedGateway> gateways = new ArrayList<>();
        SoftwareDefinedGateway g1 = client.querySoftwareDefinedGateway_Unicast("2fb8ecfa-3a8c-4d23-b9e3-0757a8121c9b");
        SoftwareDefinedGateway g2 = client.querySoftwareDefinedGateway_Unicast("3db5d4ed-0d9b-4ad2-90c8-98466ec38140");
        gateways.add(g1);
        gateways.add(g2);

        (new CacheGateway()).writeGatewayCache(gateways);
//        List<VNF> routers = client.queryVNFBroadcast();
        List<VNF> routers = new ArrayList<>();
        VNF r1 = client.queryVNF_Unicast("ed45039e-a5dc-4c08-b960-b41c47577310"); // cloud        
        VNF r2 = client.queryVNF_Unicast("ded3f818-f05c-42ea-904b-3e01ef1f154f"); // virtual router 1
        VNF r3 = client.queryVNF_Unicast("e7a0bdc3-2151-432b-93f3-4e1c9082a88b"); // iot site 1
        routers.add(r1);
        routers.add(r2);
        routers.add(r3);
        (new CacheVNF()).writeGatewayCache(routers);
        System.out.println("Gateway number: " + gateways.size());
        System.out.println("Router number: " + routers.size());

        NetworkGraphGenerator generator = new NetworkGraphGenerator();
        String graph = generator.generateGraph(gateways, routers);
        System.out.println(graph);
    }
}
