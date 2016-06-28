/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.android;

import android.hardware.AndroidSensor;

import java.util.List;
import sinc.hinc.abstraction.transformer.IoTResourceTransformation;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ExecutionEnvironment;

/**
 * The transformer (should be renamed to DataPoint constructor later) that get data from the domain model and build the DataPoint
 *
 * @author hungld
 */
public class AndroidSensorTransformer implements IoTResourceTransformation<AndroidSensor> {

    @Override
    public DataPoint updateDataPoint(AndroidSensor data) {
        DataPoint datapoint = new DataPoint(data.getmName(), data.getmName(), "type:" + data.getmType() + ",version:" + data.getmVersion());
        datapoint.setMeasurementUnit("default_unit_for_android_type:" + data.getmType());
//        datapoint.setRate(data.getmMinDelay());
        datapoint.setDatatype("type:" + data.getmType());
        return datapoint;
    }

    // return null that means the resource have no such capability
    @Override
    public List<ControlPoint> updateControlPoint(AndroidSensor data) {
        return null;
    }

    @Override
    public ExecutionEnvironment updateExecutionEnvironment(AndroidSensor data) {
        return null;
    }

    @Override
    public CloudConnectivity updateCloudConnectivity(AndroidSensor data) {
        return null;
    }

}
