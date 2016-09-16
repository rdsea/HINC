/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import com.orientechnologies.orient.core.record.impl.ODocument;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import sinc.hinc.repository.Utils;

/**
 *
 * @author hungld
 */
public class IoTUnitMapper implements DTOMapperInterface<IoTUnit> {

    @Override
    public IoTUnit fromODocument(ODocument doc) {
        IoTUnit unit = new IoTUnit();

        unit.setHincID(String.valueOf(doc.field("hincid")));
        unit.setResourceID(String.valueOf(doc.field("resourceid")));
        unit.setMeta(Utils.jsonToMap(String.valueOf(doc.field("meta"))));
        unit.setState(Utils.jsonToMap(String.valueOf(doc.field("state"))));
        unit.hasPhysicalType(String.valueOf(doc.field("physicaltype")));
        return unit;
    }

    @Override
    public ODocument toODocument(IoTUnit object) {
        ODocument doc = new ODocument();
        doc.setClassName(IoTUnit.class.getSimpleName());

        // the method getUuid only to save in DB for querying, not need to read back
        doc.field("uuid", object.getUuid());
        doc.field("hincid", object.getHincID());
        doc.field("resourceid", object.getResourceID());
        doc.field("meta", Utils.mapToJson(object.getMeta()));
        doc.field("state", Utils.mapToJson(object.getState()));
        doc.field("physicaltype", object.getPhysicalType());

        return doc;
    }

}
