/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import com.orientechnologies.orient.core.record.impl.ODocument;
import java.util.ArrayList;
import java.util.Arrays;
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
        unit.setPhysicalResourceUuid(new ArrayList<>(Arrays.asList(String.valueOf(doc.field("resourceids")).split(","))));        
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
        StringBuilder sb = new StringBuilder();
        for(String s: object.getPhysicalResourceUuid()){
            sb.append(",").append(s);
        }        
        doc.field("resourceids", sb.toString());
        doc.field("meta", Utils.mapToJson(object.getMeta()));
        doc.field("state", Utils.mapToJson(object.getState()));
        doc.field("physicaltype", object.getPhysicalType());

        return doc;
    }

}
