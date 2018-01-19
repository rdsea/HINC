/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource.Capabilities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import java.util.Objects;
import sinc.hinc.model.API.HINCPersistable;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;

/**
 *
 * @author hungld
 */
public class DataPoint implements HINCPersistable {

    // uuid is for internal identifying, name is for human identifying
    String iotUnitID;
    String name;
    // temperature, humidity, GPS, image, video_streaming, state
    String datatype;

    // if available based on type
    String measurementUnit;

    // reading rate
//    int rate;
    /**
     * Any model to represent how the data can be read. This can be domain
     * model, MQTT/AMPQ persistent, streamming to read/write data. See more in
     * the package sinc.hinc.model.Extensible.Reading
     */
    String dataApi;
    Map<String, String> dataApiSettings;

    /**
     * The data point is forward to somewhere.
     */
    AccessPoint connectingTo;

    public DataPoint() {

    }

    /**
     * In the case we have 1 parameter, it is data type. Use this for building
     * data point template.
     *
     * @param name
     * @param dataType
     */
    public DataPoint(String name, String dataType) {
        this.name = name;
        this.datatype = dataType;
    }

    // to have this field only for Jackson to work properly
    String uuid;

    @Override
    public String getUuid() {
        this.uuid = this.iotUnitID + "/" + name;
        return this.uuid;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public String getDataApi() {
        return dataApi;
    }

    public void setDataApi(String dataApi) {
        this.dataApi = dataApi;
    }

    public Map<String, String> getDataApiSettings() {
        return dataApiSettings;
    }

    @JsonIgnore
    public String getDataAPISettingsPlain() {
        return dataApiSettings.toString();
    }

    public void setDataApiSettings(Map<String, String> dataApiSettings) {
        this.dataApiSettings = dataApiSettings;
    }

//    public List<ControlPoint> getControlpoints() {
//        if (controlpoints == null) {
//            controlpoints = new ArrayList<>();
//        }
//        return controlpoints;
//    }
//
//    public void setControlpoints(List<ControlPoint> controlpoints) {
//        this.controlpoints = controlpoints;
//    }
//
//    public ControlPoint getControlByName(String name) {
//        if (this.controlpoints != null) {
//            for (ControlPoint cp : this.controlpoints) {
//                if (cp.getName().equals(name)) {
//                    return cp;
//                }
//            }
//        }
//        return null;
//    }
    public AccessPoint getConnectingTo() {
        return connectingTo;
    }

    public void setConnectingTo(AccessPoint connectingTo) {
        this.connectingTo = connectingTo;
    }

    public String getIotUnitID() {
        return iotUnitID;
    }

    public void setIotUnitID(String iotUnitID) {
        this.iotUnitID = iotUnitID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.iotUnitID);
        hash = 89 * hash + Objects.hashCode(this.name);
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
        final DataPoint other = (DataPoint) obj;
        if (!Objects.equals(this.iotUnitID, other.iotUnitID)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

}
