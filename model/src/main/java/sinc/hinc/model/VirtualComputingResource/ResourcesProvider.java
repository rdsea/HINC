/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource;

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

    // will be imported from the adaptor
    Map<String, String> settings;

    // use control point to capture APIs
    List<ControlPoint> apis = new ArrayList<>();

    public ResourcesProvider() {
        this.settings = new HashMap<>();
    }

    public ResourcesProvider(Map<String, String> settings) {
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

}
