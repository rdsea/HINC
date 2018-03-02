/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import com.orientechnologies.orient.core.record.impl.ODocument;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.repository.Utils;

/**
 *
 * @author hungld
 */
public class ControlPointMapper implements DTOMapperInterface<ControlPoint> {

    @Override
    public ControlPoint fromODocument(ODocument doc) {
        ControlPoint con = new ControlPoint();

        con.setName(String.valueOf(doc.field("name")));
        con.setIotUnitID(String.valueOf(doc.field("iotunituuid")));

        con.setInvokeProtocol(ControlPoint.InvokeProtocol.valueOf(doc.field("invokeprotocol").toString()));
        con.setParameters(Utils.jsonToMap(String.valueOf(doc.field("parameters"))));
        con.setReference(String.valueOf(doc.field("reference")));

        con.setControlType(ControlPoint.ControlType.valueOf((String.valueOf(doc.field("controlType")))));
        con.setEffects(Utils.jsonToMap(String.valueOf(doc.field("effects"))));
        con.setConditions(Utils.jsonToMap(String.valueOf(doc.field("conditions"))));
        return con;
    }

    @Override
    public ODocument toODocument(ControlPoint object) {
        ODocument doc = new ODocument();
        doc.setClassName(ControlPoint.class.getSimpleName());
        doc.field("uuid", object.getUuid());
        doc.field("name", object.getName());
        doc.field("iotunituuid", object.getIotUnitID());

        doc.field("invokeprotocol", object.getInvokeProtocol().toString());
        doc.field("parameters", Utils.mapToJson(object.getParameters()));
        doc.field("reference", object.getReference());

        doc.field("controlType", object.getControlType());
        doc.field("effects", Utils.mapToJson(object.getEffects()));
        doc.field("conditions", Utils.mapToJson(object.getConditions()));
        return doc;
    }

}
