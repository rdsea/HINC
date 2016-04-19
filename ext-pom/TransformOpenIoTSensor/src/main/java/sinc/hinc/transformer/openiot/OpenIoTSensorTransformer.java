/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openiot;

import sinc.hinc.transformer.openiot.model.OpenIoTSensor;
import sinc.hinc.transformer.openiot.model.OpenIoTSensorWrapper;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import java.util.List;
import sinc.hinc.abstraction.transformer.GatewayResourceTransformationInterface;

/**
 *
 * @author hungld
 */
public class OpenIoTSensorTransformer implements GatewayResourceTransformationInterface<OpenIoTSensorWrapper> {

    @Override
    public OpenIoTSensorWrapper validateAndConvertToDomainModel(String data, String dataSource) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(data, OpenIoTSensorWrapper.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

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
