/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.android;

import android.hardware.AndroidSensor;

import sinc.hinc.abstraction.transformer.DataPointTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;

/**
 * The transformer (should be renamed to DataPoint constructor later) that get
 * data from the domain model and build the DataPoint
 *
 * @author hungld
 */
public class AndroidSensorTransformer implements DataPointTransformer<AndroidSensor> {

    @Override
    public DataPoint updateDataPoint(AndroidSensor data) {
        DataPoint datapoint = new DataPoint(data.getmName(), data.getmName(), "type:" + data.getmType() + ",version:" + data.getmVersion());
        datapoint.setMeasurementUnit("default_unit_for_android_type:" + data.getmType());
//        datapoint.setRate(data.getmMinDelay());
        datapoint.setDatatype("type:" + data.getmType());
        return datapoint;
    }

    @Override
    public String getName() {
        return "android";
    }

}
