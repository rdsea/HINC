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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Hong-Linh Truong
 */
/*
* Sample of a rig device can be found in the test directory.
* The TestRig is based on a real Test Rig implemented at Nordic Med Test. 
* The Test Rig is part of the H2020 U-Test project. 
* We use it as an example of IoT provider for HINC in the context of H2020 Inter-IoT as 
* it supports both datapoints and control points 
* of IoT devices.
 */
public class TestRigMetadataItem {

    static Logger logger = LoggerFactory.getLogger("TestRig");

    String id;
    String device_type;
    String url = null;
    Map<String, String> distance = new HashMap<>();
    Map<String, String> heartrate = new HashMap<>();
    Map<String, String> position = new HashMap<>();
    Map<String, String> move = new HashMap<>();
    Map<String, String> voltage = new HashMap<>();
    String method= null; //this indicates control APIs
    String turn_locator=null;
    String in_motion;

    public String getIn_motion() {
        return in_motion;
    }

    public void setIn_motion(String in_motion) {
        this.in_motion = in_motion;
    }
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

    public Map<String, String> getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(Map<String, String> heartrate) {
        this.heartrate = heartrate;
    }

    public Map<String, String> getPosition() {
        return position;
    }

    public void setPosition(Map<String, String> position) {
        this.position = position;
    }

    public Map<String, String> getMove() {
        return move;
    }

    public void setMove(Map<String, String> move) {
        this.move = move;
    }

    public Map<String, String> getVoltage() {
        return voltage;
    }

    public void setVoltage(Map<String, String> voltage) {
        this.voltage = voltage;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTurn_locator() {
        return turn_locator;
    }

    public void setTurn_locator(String turn_locator) {
        this.turn_locator = turn_locator;
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
