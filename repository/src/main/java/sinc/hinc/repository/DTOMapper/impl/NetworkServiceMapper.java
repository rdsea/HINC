/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import com.orientechnologies.orient.core.record.impl.ODocument;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;

/**
 *
 * @author hungld
 */
public class NetworkServiceMapper implements DTOMapperInterface<NetworkFunctionService> {

    @Override
    public NetworkFunctionService fromODocument(ODocument doc) {
        NetworkFunctionService ns = new NetworkFunctionService();    
        ns.setName(String.valueOf(doc.field("name")));
        ns.setType(NetworkFunctionService.NetworkServiceType.valueOf(String.valueOf(doc.field("type"))));
        ns.setAccessPoint(new AccessPoint(String.valueOf(doc.field("accesspointendpoint"))));
        ns.setUuid(String.valueOf(doc.field("uuid")));
        System.out.println("Return a network object: " + ns.toJson());
        System.out.println("  --> Read the doc.field(\"accesspointendpoint\"): " + doc.field("accesspointendpoint"));
        return ns;
    }

    @Override
    public ODocument toODocument(NetworkFunctionService object) {
        System.out.println("Persisting a network service: " + object.toJson());
        System.out.println("   --> accesspointendpoint = " + object.getAccessPoint().getEndpoint());
        ODocument doc = new ODocument(NetworkFunctionService.class.getSimpleName());
        doc.field("name", object.getName());
        doc.field("type", object.getType().toString());
        doc.field("accesspointendpoint", object.getAccessPoint().getEndpoint());
        doc.field("uuid", object.getUuid());
        return doc;
    }

}
