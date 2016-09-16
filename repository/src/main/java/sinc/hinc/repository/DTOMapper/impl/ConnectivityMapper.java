/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import com.orientechnologies.orient.core.record.impl.ODocument;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;

/**
 *
 * @author hungld
 */
public class ConnectivityMapper implements DTOMapperInterface<CloudConnectivity> {

    @Override
    public CloudConnectivity fromODocument(ODocument doc) {

        CloudConnectivity con = new CloudConnectivity();
//        con.setDefaultGateway(String.valueOf(doc.field("defaultgateway")));
//        con.setIP(String.valueOf(doc.field("ip")));
//        con.setMAC(String.valueOf(doc.field("mac")));
//        con.setMode(String.valueOf(doc.field("mode")));
        return con;
    }

    @Override
    public ODocument toODocument(CloudConnectivity object) {

        ODocument doc = new ODocument();

        doc.setClassName(CloudConnectivity.class.getSimpleName());
//        doc.field("defaultgatewayip", object.getDefaultGateway());
//        doc.field("ip", object.getIP());
//        doc.field("mac", object.getMAC());
//        doc.field("mode", object.getMode());

        return doc;
    }

}
