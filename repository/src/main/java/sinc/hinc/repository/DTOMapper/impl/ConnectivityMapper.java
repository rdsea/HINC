/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 *
 * @author hungld
 */
public class ConnectivityMapper implements DTOMapperInterface<CloudConnectivity> {

    @Override
    public CloudConnectivity fromODocument(ODocument doc) {
        CapabilityMapper capa = new CapabilityMapper(CloudConnectivity.class);
        CloudConnectivity con = (CloudConnectivity) capa.fromODocument(doc);
        con.setDefaultGateway(String.valueOf(doc.field("defaultgateway")));
        con.setIP(String.valueOf(doc.field("ip")));
        con.setMAC(String.valueOf(doc.field("mac")));
        con.setMode(String.valueOf(doc.field("mode")));
        return con;
    }

    @Override
    public ODocument toODocument(CloudConnectivity object) {
        CapabilityMapper capa = new CapabilityMapper(CloudConnectivity.class);
        ODocument doc = capa.toODocument(object);
        doc.setClassName(CloudConnectivity.class.getSimpleName());
        doc.field("defaultgatewayip", object.getDefaultGateway());
        doc.field("ip", object.getIP());
        doc.field("mac", object.getMAC());
        doc.field("mode", object.getMode());

        return doc;
    }

}
