/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;

/**
 * To capture provider API
 *
 * @author hungld
 */
public class ResourcesProvider {

    String name;
    String uri;

    // will be imported from the adaptor
    Map<String, String> settings;

    // use control point to capture APIs
    List<ControlPoint> apis = new ArrayList<>();

    public ResourcesProvider() {
    }

    public ResourcesProvider(String name, String uri) {
        this.name = name;
        this.uri = uri;
        this.settings = new HashMap<>();
    }

    public ResourcesProvider(String name, String uri, Map<String, String> settings) {
        this.name = name;
        this.uri = uri;
        this.settings = settings;
    }

    public ResourcesProvider hasSettings(String key, String value) {
        this.settings.put(key, value);
        return this;
    }

    public ResourcesProvider hasApi(ControlPoint cp) {
        this.apis.add(cp);
        return this;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

    public List<ControlPoint> getApis() {
        return apis;
    }

    public void setApis(List<ControlPoint> apis) {
        this.apis = apis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
     public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResourcesProvider fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, ResourcesProvider.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

}
