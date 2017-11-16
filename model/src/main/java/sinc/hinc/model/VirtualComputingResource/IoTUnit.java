/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import sinc.hinc.model.API.HINCPersistable;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;

/**
 * The IoTUnit abstracts multiple low level resources into a model related to
 * data, control, connectivity. Some addition information: metadata, state, main
 * physical device.
 *
 * @author hungld
 */
public class IoTUnit implements HINCPersistable {

    /**
     * Link to some external identification where this resource get info from.
     * E.g. to the real sensors/actuators ID (also captured in PhysicalResource)
     */
    List<String> physicalResourceUuid = new ArrayList<>();

    /**
     * hincID is used for internal management, to detect which HINC is managing
     * the resource
     */
    String hincID;

    // the static information
    Map<String, String> meta;
    // the dynamic state, use by the control point management
    Map<String, String> state;

    // to specify which is the *MAIN* physical device this iot unit aim to
    String physicalType;

    Set<DataPoint> datapoints = new HashSet<>();
    Set<ControlPoint> controlpoints = new HashSet<>();
    Set<CloudConnectivity> connectivities;

    public IoTUnit() {
    }

    // to have this field only for Jackson to work properly
    String uuid = UUID.randomUUID().toString();

    @Override
    public String getUuid() {
        return this.uuid;
    }

    public String getHincID() {
        return hincID;
    }

    public void setHincID(String hincID) {
        this.hincID = hincID;
    }

    public void setPhysicalType(String physicalType) {
        this.physicalType = physicalType;
    }
    
    public IoTUnit hasPhysicalResourceUuid(String physicalRsUuid){
        this.physicalResourceUuid.add(physicalRsUuid);
    }

    public IoTUnit hasDatapoint(DataPoint dp) {
        if (datapoints == null) {
            this.datapoints = new HashSet<>();
        }
        dp.getResourceUuid().add(this.getUuid());
        this.datapoints.add(dp);
        return this;
    }

    public void setDatapoints(Set<DataPoint> datapoints) {
        for (DataPoint dp : datapoints) {
            dp.getResourceUuid().add(this.getUuid());
        }
        this.datapoints = datapoints;
    }

    public IoTUnit hasControlPoint(ControlPoint cp) {
        if (controlpoints == null) {
            this.controlpoints = new HashSet<>();
        }
        cp.getResourceUuid().add(this.getUuid());
        this.controlpoints.add(cp);
        return this;
    }

    public IoTUnit hasControlPoints(ControlPoint... cps) {
        for (ControlPoint cp : cps) {
            hasControlPoint(cp);
        }
        return this;
    }

    public void setControlpoints(Set<ControlPoint> controlpoints) {
        for (ControlPoint cp : controlpoints) {
            cp.getResourceUuid().add(this.getUuid()
            );
        }
        this.controlpoints = controlpoints;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public IoTUnit hasMeta(String key, String value) {
        if (this.meta == null) {
            this.meta = new HashMap<>();
        }
        this.meta.put(key, value);
        return this;
    }

    public IoTUnit hasMeta(Map<String, String> metaMap) {
        if (this.meta == null) {
            this.meta = new HashMap<>();
        }
        this.meta.putAll(metaMap);
        return this;
    }

    public Set<DataPoint> getDatapoints() {
        return datapoints;
    }

    // TODO: hack as we assume one IoTUnit has one datapoint only
    @JsonIgnore
    public DataPoint getFirstDatapoint() {
        if (this.datapoints != null && !this.datapoints.isEmpty()) {
            return this.datapoints.iterator().next();
        }
        return null;
    }

    public Set<ControlPoint> getControlpoints() {
        return controlpoints;
    }

    public Map<String, String> getState() {
        if (this.state == null) {
            this.state = new HashMap<>();
        }
        return state;
    }

    public Set<CloudConnectivity> getConnectivities() {
        return connectivities;
    }

    public void setConnectivities(Set<CloudConnectivity> connectivities) {
        this.connectivities = connectivities;
    }

    public ControlPoint findControlpointByName(String controlName) {
        for (ControlPoint c : controlpoints) {
            if (c.getName().toUpperCase().equals(controlName.toUpperCase())) {
                return c;
            }
        }
        return null;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public void setState(Map<String, String> state) {
        this.state = state;
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

    public static IoTUnit fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, IoTUnit.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public String getPhysicalType() {
        return physicalType;
    }

    public IoTUnit hasPhysicalType(String physicalType) {
        this.physicalType = physicalType;
        return this;
    }

    public List<String> getPhysicalResourceUuid() {
        return physicalResourceUuid;
    }

    public void setPhysicalResourceUuid(List<String> physicalResourceUuid) {
        this.physicalResourceUuid = physicalResourceUuid;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.physicalResourceUuid);
        hash = 83 * hash + Objects.hashCode(this.hincID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IoTUnit other = (IoTUnit) obj;
        if (!Objects.equals(this.hincID, other.hincID)) {
            return false;
        }
        if (!Objects.equals(this.physicalResourceUuid, other.physicalResourceUuid)) {
            return false;
        }
        return true;
    }

}
