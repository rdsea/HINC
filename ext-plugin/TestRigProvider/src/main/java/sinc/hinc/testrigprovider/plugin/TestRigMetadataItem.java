/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.testrigprovider.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author linhsolar
 */
/*
Sample of a rig device
{
        "device_type": "rig device", 
        "distance": {
            "url": "..../rig/cart_2/distance/"
        }, 
        "heartrate": {
            "url": ".../rig/cart_2/heartrate"
        }, 
        "id": "cart_2", 
        "in motion": false, 
        "move": {
            "url": ".../rig/cart_2/move"
        }, 
        "position": {
            "url": ".../rig/cart_2/position"
        }, 
        "url": ".../rig/cart_2"
    }, 
*/
public class TestRigMetadataItem {

    static Logger logger = LoggerFactory.getLogger("TestRig");

    String id ;
    String device_type ;

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    String url = null;
    Map<String, String> distance = new HashMap<>();

  

  

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

  


    public Map<String, String> getDistance() {
        return distance;
    }

    public void setDistance(Map<String, String> distance) {
        this.distance = distance;
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
