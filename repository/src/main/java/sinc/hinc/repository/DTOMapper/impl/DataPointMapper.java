/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.core.record.impl.ODocument;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.repository.DTOMapper.DTOMapperInterface;

/**
 *
 * @author hungld
 */
public class DataPointMapper implements DTOMapperInterface<DataPoint> {

    @Override
    public DataPoint fromODocument(ODocument doc) {
        DataPoint dp = new DataPoint();
        dp.setName(String.valueOf(doc.field("name")));
        dp.setIotUnitID(String.valueOf(doc.field("iotunituuid")));
        dp.setDatatype(String.valueOf(doc.field("datatype")));
        dp.setMeasurementUnit(String.valueOf(doc.field("measurementunit")));
        dp.setDataApi(String.valueOf(doc.field("dataapi")));

        // TODO: change to Utils. Just to be sure that it can run
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
        };
        try {
            Map<String, String> settings = mapper.readValue(String.valueOf(doc.field("dataapisettings")), typeRef);
            dp.setDataApiSettings(settings);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dp;
    }

    @Override
    public ODocument toODocument(DataPoint object) {
        ODocument doc = new ODocument();
        doc.field("uuid", object.getUuid());
        doc.field("name", object.getName());
        doc.field("iotunituuid", object.getIotUnitID());
        doc.setClassName(DataPoint.class.getSimpleName());
        doc.field("datatype", object.getDatatype());
        doc.field("measurementunit", object.getMeasurementUnit());
        doc.field("dataapi", object.getDataApi());

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
        };
        String settings;
        try {
            settings = mapper.writeValueAsString(object.getDataApiSettings());
            doc.field("dataapisettings", settings);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        return doc;
    }

}
