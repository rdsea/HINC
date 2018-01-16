/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.testrigprovider.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 *
 * @author Hong-Linh Truong
 */
public class RigDeviceData {

    static Logger logger = LoggerFactory.getLogger("TestRig");
    List<TestRigMetadataItem> devices = new ArrayList<>();

    public RigDeviceData() {
    }

    public List<TestRigMetadataItem> getDevices() {
        return devices;
    }

    public void setDevices(List<TestRigMetadataItem> devices) {
        this.devices = devices;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    /*
    * Only for testing purpose
     */
    public static void main(String[] args) throws JsonProcessingException, FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<TestRigMetadataItem> devices = mapper.readValue(reader, new TypeReference<List<TestRigMetadataItem>>() {});
        RigDeviceData data = new RigDeviceData();
        data.setDevices(devices);
//RigDeviceData data = mapper.readValue(reader, RigDeviceData.class);
        TestRigProviderTransformer transformer = new TestRigProviderTransformer();
        for (TestRigMetadataItem item : data.devices) {
            logger.debug(mapper.writeValueAsString(item));
            IoTUnit unit = transformer.translateIoTUnit(item);
            logger.debug(unit.toJson());
        }

    }

}
