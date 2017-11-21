/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.testrigprovider.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static sinc.hinc.testrigprovider.plugin.TestRigMetadataItem.logger;

/**
 *
 * @author hungld
 */
public class RigDeviceData {

    static Logger logger = LoggerFactory.getLogger("TestRig");
    List<TestRigMetadataItem> devices = new ArrayList<>();

 
 
    public RigDeviceData() {
    }

    public List<TestRigMetadataItem> getDevices() {
        return devices;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }
 
      public static void main(String[] args) throws JsonProcessingException, FileNotFoundException, IOException {
          BufferedReader reader = new BufferedReader(new FileReader(args[1]));
          ObjectMapper mapper = new ObjectMapper();
          RigDeviceData data = mapper.readValue(reader, RigDeviceData.class);
          for (TestRigMetadataItem item:data.devices){
              logger.debug(mapper.writeValueAsString(item));
          }
        
       
    }

}
