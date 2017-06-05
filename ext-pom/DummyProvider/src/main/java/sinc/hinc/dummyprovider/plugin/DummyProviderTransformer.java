/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.plugin;

import sinc.hinc.abstraction.transformer.IoTUnitTransformer;
import sinc.hinc.dummyprovider.provider.DummyMetadataItem;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 *
 * @author hungld
 */
public class DummyProviderTransformer implements IoTUnitTransformer<DummyMetadataItem> {

    @Override
    public IoTUnit translateIoTUnit(DummyMetadataItem data) {
        IoTUnit unit = new IoTUnit();
        unit.setResourceID(data.getId());
        DataPoint dp = new DataPoint(data.getName(), data.getType());
        dp.setMeasurementUnit(data.getUnit());
        unit.hasDatapoint(dp);
        return unit;
    }

    @Override
    public String getName() {
        return "dummy";
    }

}
