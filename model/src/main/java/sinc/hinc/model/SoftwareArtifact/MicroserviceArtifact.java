/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.SoftwareArtifact;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hungld
 * @author linhsolar
 */
/*
 * We use this one to describe possible software artifact that can be deployed as microservice
 * The artifact is available in some repositories. It is not provided by any provider but 
 * as long as we want we can take it and deploy it. in rsiHUB it is used to deploy bridge 
 * for interoperability. The artifacts and their instances can be combined into pipelines 
 * to perform complex work
 */
public class MicroserviceArtifact {

    //indicate where is the artifact
    String sourceEndpoint;
    //indicate runtime information when the artifact is deployed
    //we should refactor it to separate betwen artifact and instance.
    String endpoint;
    String resourceID;
    String name;
    String hostID;
    Map<String, String> meta;

    public String getSourceEndpoint() {
        return sourceEndpoint;
    }

    public void setSourceEndpoint(String sourceEndpoint) {
        this.sourceEndpoint = sourceEndpoint;
    }
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostID() {
        return hostID;
    }

    public void setHostID(String hostID) {
        this.hostID = hostID;
    }

    public MicroserviceArtifact hasMeta(String key, String value) {
        if (this.meta == null) {
            this.meta = new HashMap<>();
        }
        this.meta.put(key, value);
        return this;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

}
