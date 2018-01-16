/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.haivancameraprovider.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author linhsolar
 * 
 */
/*
* Sample of data
id	"2co2.vp9.tv@chn@DNG33"
name	"Camera gần Trường Mầm Non Tiên Sa"
description	"view 1 - DNG33 (1010 IPC7)"
address	"106 Quang Trung, Phường …n Hải Châu, TP. Đà Nẵng"
phonenumber	"+84 236 3822348"
type	"video"
datapoint	"http://2co2.vp9.tv/chn/DNG33"
datapoint-controller	"dng-camera-provider"
fps	"20"
conn	"[object Object],[object Object],[object Object]"
*/
public class CameraMetadataItem {

    static Logger logger = LoggerFactory.getLogger("HaivanCamera");

    String id = null;
    String address = null;
    String type = null;
    String name = null;  
    String description =  null;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDatapoint() {
        return datapoint;
    }

    public void setDatapoint(String datapoint) {
        this.datapoint = datapoint;
    }
    
    String datapoint = null;
    Map<String, String> metadata = new HashMap<>();
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

 

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

}
