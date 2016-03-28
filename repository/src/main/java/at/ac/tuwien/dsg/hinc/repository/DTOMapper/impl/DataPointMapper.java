/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl;

import at.ac.tuwien.dsg.hinc.repository.DTOMapper.DTOMapperInterface;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.CapabilityType;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 *
 * @author hungld
 */
public class DataPointMapper implements DTOMapperInterface<DataPoint>{

    @Override
    public DataPoint fromODocument(ODocument doc) {
        DataPoint dp = new DataPoint();
        dp.setName(doc.field("name").toString());
        dp.setDatatype(doc.field("datatype").toString());
        dp.setDescription(doc.field("description").toString());
        dp.setMeasurementUnit(doc.field("measurementunit").toString());
        dp.setResourceID(doc.field("resourceid").toString());
        dp.setGatewayID(doc.field("gatewayid").toString());        
        return dp;
    }

    @Override
    public ODocument toODocument(DataPoint object) {
        ODocument doc = new ODocument("SoftwareDefinedGateway");
        doc.field("resourceid", object.getResourceID());
        doc.field("gatewayid", object.getGatewayID());
        doc.field("measurementunit", object.getMeasurementUnit());
        doc.field("description", object.getDescription());
        doc.field("datatype", object.getDatatype());
        doc.field("name", object.getName());        
        return doc;
    }
    
}
