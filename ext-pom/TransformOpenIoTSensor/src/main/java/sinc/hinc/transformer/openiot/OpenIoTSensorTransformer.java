/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openiot;

import sinc.hinc.transformer.openiot.model.OpenIoTSensor;
import sinc.hinc.transformer.openiot.model.OpenIoTSensorWrapper;

import java.util.List;
import sinc.hinc.abstraction.transformer.IoTResourceTransformation;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ExecutionEnvironment;

/**
 *
 * @author hungld
 */
public class OpenIoTSensorTransformer implements IoTResourceTransformation<OpenIoTSensorWrapper> {

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
    public List<ControlPoint> updateControlPoint(OpenIoTSensorWrapper data) {
        return null;
    }

    @Override
    public ExecutionEnvironment updateExecutionEnvironment(OpenIoTSensorWrapper data) {
        return null;
    }

    @Override
    public CloudConnectivity updateCloudConnectivity(OpenIoTSensorWrapper data) {
        return null;
    }

}
