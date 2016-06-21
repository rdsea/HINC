/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource.Capabilities;

import sinc.hinc.model.VirtualComputingResource.Capability;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hungld
 */
public class ExecutionEnvironment extends Capability {

    public static enum EnvironmentType {
        JRE, Python, apache2
    }

    EnvironmentType env;

    // e.g. version, status (on/off), settings
    Map<String, String> attributes;

    /**
     * **************
     * GETER/SETTER * **************
     */
    public ExecutionEnvironment() {

    }

    public ExecutionEnvironment(String resourceID, String name, String description) {
        super(resourceID, name, description);
    }

    public ExecutionEnvironment(String resourceID, String name, String description, EnvironmentType envType) {
        super(resourceID, name, description);
        this.env = envType;
    }

    public EnvironmentType getEnv() {
        return env;
    }

    public void setEnv(EnvironmentType env) {
        this.env = env;
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

}
