/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.haivancameraprovider.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hungld
 */
public class CameraIoTDevices {

    static Logger logger = LoggerFactory.getLogger("HaivanCameraDevice");
    List<CameraMetadataItem> cameras = new ArrayList<>();

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    public CameraIoTDevices() {
    }
    //return the list of cameras
    public List<CameraMetadataItem> getDevices() {
        return cameras;
    }


}
