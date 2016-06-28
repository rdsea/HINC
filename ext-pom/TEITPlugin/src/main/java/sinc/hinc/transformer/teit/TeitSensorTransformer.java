/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.teit;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import sinc.hinc.abstraction.transformer.IoTResourceTransformation;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ExecutionEnvironment;

/**
 *
 * @author hungld
 */
public class TeitSensorTransformer implements IoTResourceTransformation<DummyMetadataItem> {

    @Override
    public DataPoint updateDataPoint(DummyMetadataItem data) {
        DataPoint dp = new DataPoint();
        dp.setName(data.getName());
        dp.setMeasurementUnit(data.getUnit());
        dp.setDescription(data.getDescription());
        dp.setDatatype(data.getType());
        dp.setResourceID(data.getId());        
        dp.setBufferType(data.getBufferType().toString());
        dp.setBufferSettings(data.getBufferSettings());
        return dp;
    }

    @Override
    public List<ControlPoint> updateControlPoint(DummyMetadataItem data) {
        return new ArrayList<>();
    }

    @Override
    public ExecutionEnvironment updateExecutionEnvironment(DummyMetadataItem data) {
        return null;
    }

    @Override
    public CloudConnectivity updateCloudConnectivity(DummyMetadataItem data) {
        return null;
    }

}
