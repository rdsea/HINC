/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource.Capabilities;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hungld
 */
public class ExecutionEnvironment {
    String name;
    String description;

    String version;

    // environment specific properties  
    Map<String, String> attributes;

    /**
     * **************
     * GETER/SETTER * **************
     */
    public ExecutionEnvironment() {

    }

    public Map<String, String> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public ExecutionEnvironment hasAttribute(String name, String value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        if (name != null && value != null && !name.trim().isEmpty() && !value.trim().isEmpty()) {
            this.attributes.put(name, value);
        }
        return this;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    

}
