/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.teit;

import sinc.hinc.abstraction.transformer.DataPointTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;

/**
 *
 * @author hungld
 */
public class TeitSensorTransformer implements DataPointTransformer<DummyMetadataItem> {

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
    public String getName() {
        return "teit";
    }

}
