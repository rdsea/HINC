/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import com.orientechnologies.orient.core.record.impl.ODocument;
import java.util.logging.Level;
import java.util.logging.Logger;
import sinc.hinc.model.VirtualComputingResource.Capability.Capability;

/**
 *
 * @author hungld
 */
public class CapabilityMapper implements DTOMapperInterface<Capability> {

    Class subClazz;

    public CapabilityMapper(Class<? extends Capability> subClazz) {
        this.subClazz = subClazz;
    }

    @Override
    public Capability fromODocument(ODocument doc) {
        Capability capa;
        try {
            capa = (Capability) subClazz.newInstance();
            capa.setName(String.valueOf(doc.field("name")));
            capa.setDescription(String.valueOf(doc.field("description")));
            capa.setGatewayID(String.valueOf(doc.field("gatewayid")));
            capa.setResourceID(String.valueOf(doc.field("resourceid")));
            capa.setUuid(String.valueOf(doc.field("uuid")));
            return capa;
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ODocument toODocument(Capability object) {
        ODocument doc = new ODocument(Capability.class.getSimpleName());
        doc.field("name", object.getName());
        doc.field("description", object.getDescription());
        doc.field("gatewayid", object.getGatewayID());
        doc.field("resourceid", object.getResourceID());
        doc.field("uuid", object.getUuid());
        return doc;
    }

}
