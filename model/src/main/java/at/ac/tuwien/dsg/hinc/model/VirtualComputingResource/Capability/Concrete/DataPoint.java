/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete;

import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Capability;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.CapabilityType;
import java.util.List;

/**
 *
 * @author hungld
 */
public class DataPoint extends Capability {

    // temperature, humidity, GPS, image, video_streaming, state
    String datatype;

    // if available based on type
    String measurementUnit;

    // reading rate
    int rate;

    /**
     * The class which implementation functions to interact with this DataPoint Some function can be: - onStateChanged() - onBufferChanged(String bufferName,
     * Object oldData, Object newData); - Stream<Object> getDataStream(String buffer); - setData(String bufferName, Object newData); - changeDataRate(DataPoint
     * datapoint, Long rate);
     */
    String managementClass;

    /**
     * List of control point for this data point
     */
    List<ControlPoint> controlpoints;

    public DataPoint() {
        capabilityType = CapabilityType.DataPoint;
    }
    
    /**
     * In the case we have 1 parameter, it is data type. Use this for building data point template.
     * @param dataType 
     */
    public DataPoint(String dataType){
        this.datatype = dataType;
    }

    public DataPoint(String resourceID, String name, String description) {
        super(resourceID, name, CapabilityType.DataPoint, description);
    }

    public DataPoint(String resourceID, String name, String description, String datatype, String measurementUnit, int rate) {
        super(resourceID, name, CapabilityType.DataPoint, description);
        this.datatype = datatype;
        this.measurementUnit = measurementUnit;
        this.rate = rate;
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

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getManagementClass() {
        return managementClass;
    }

    public void setManagementClass(String managementClass) {
        this.managementClass = managementClass;
    }

    public List<ControlPoint> getControlpoints() {
        return controlpoints;
    }

    public void setControlpoints(List<ControlPoint> controlpoints) {
        this.controlpoints = controlpoints;
    }

    public ControlPoint getControlByName(String name) {
        if (this.controlpoints != null) {
            for (ControlPoint cp : this.controlpoints) {
                if (cp.getName().equals(name)) {
                    return cp;
                }
            }
        }
        return null;
    }

}
