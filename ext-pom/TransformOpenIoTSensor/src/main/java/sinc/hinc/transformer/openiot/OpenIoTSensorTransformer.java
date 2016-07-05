/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openiot;

import sinc.hinc.transformer.openiot.model.OpenIoTSensor;
import sinc.hinc.transformer.openiot.model.OpenIoTSensorWrapper;

import sinc.hinc.abstraction.transformer.DataPointTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;

/**
 *
 * @author hungld
 */
public class OpenIoTSensorTransformer implements DataPointTransformer<OpenIoTSensorWrapper> {

    @Override
    public DataPoint updateDataPoint(OpenIoTSensorWrapper wrapper) {
        if (wrapper==null){
            System.out.println("Something happen, data is null");
            return null;
        }
        OpenIoTSensor data = wrapper.getData();
        if (data==null){
            System.out.println("Something happen, data is null");
        }
        DataPoint datapoint = new DataPoint(data.getAsset().getName(), data.getAsset().getName(), data.getAsset().getDescription());
        datapoint.setDatatype(data.getModel().toString());
//        if (data.getSensorData() != null && data.getSensorData().getMs() != null) {
//            datapoint.setMeasurementUnit(data.getSensorData().getMs().getU());
//        }
        return datapoint;
    }

    @Override
    public String getName() {
        return "openiot";
    }

}
