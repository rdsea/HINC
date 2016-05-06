/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 *
 * @author hungld
 */
public class DataPointMapper implements DTOMapperInterface<DataPoint> {

    @Override
    public DataPoint fromODocument(ODocument doc) {
        CapabilityMapper capa = new CapabilityMapper(DataPoint.class);
        DataPoint dp = (DataPoint) capa.fromODocument(doc);
        dp.setUuid(String.valueOf(doc.field("uuid")));
        dp.setName(String.valueOf(doc.field("name")));
        dp.setDatatype(String.valueOf(doc.field("datatype")));
        dp.setDescription(String.valueOf(doc.field("description")));
        dp.setMeasurementUnit(String.valueOf(doc.field("measurementunit")));
        dp.setResourceID(String.valueOf(doc.field("resourceid")));
        dp.setGatewayID(String.valueOf(doc.field("gatewayid")));
        dp.setLink(String.valueOf(doc.field("link")));
        
        return dp;
    }

    @Override
    public ODocument toODocument(DataPoint object) {
        CapabilityMapper capa = new CapabilityMapper(DataPoint.class);
        ODocument doc = capa.toODocument(object);
        doc.setClassName(DataPoint.class.getSimpleName());
        doc.field("uuid", object.getUuid());
        doc.field("name", object.getName());
        doc.field("datatype", object.getDatatype());
        doc.field("description", object.getDescription());
        doc.field("measurementunit", object.getMeasurementUnit());
        doc.field("resourceid", object.getResourceID());
        doc.field("gatewayid", object.getGatewayID());
        doc.field("link", object.getLink());

        return doc;
    }

}
