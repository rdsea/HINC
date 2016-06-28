/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource.Capabilities;

import java.util.HashMap;
import java.util.Map;
import sinc.hinc.model.VirtualComputingResource.Capability;

/**
 *
 * @author hungld
 */
public class DataChannel extends Capability {

    String created;
    String updated;
    Map<Integer,DataPoint> datapoints = new HashMap<>();

    public DataChannel() {
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    
    

}
