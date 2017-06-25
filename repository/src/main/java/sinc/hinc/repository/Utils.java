/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;

/**
 *
 * @author hungld
 */
public class Utils {

    static TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
    };

    static TypeReference<List<String>> stringTypeRef = new TypeReference<List<String>>() {
    };

    static TypeReference<List<ControlPoint>> controlPointTypeRef = new TypeReference<List<ControlPoint>>() {
    };

    public static String mapToJson(Map<String, String> map) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> jsonToMap(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, typeRef);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String ListToJson(List<String> list) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<String> jsonToList(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, stringTypeRef);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String ControlPointListToJson(List<ControlPoint> list) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<ControlPoint> jsonToListControlPoint(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, controlPointTypeRef);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
