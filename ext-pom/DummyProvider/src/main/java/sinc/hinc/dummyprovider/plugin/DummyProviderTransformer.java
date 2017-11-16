/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.plugin;

import java.util.HashMap;
import java.util.Map;
import sinc.hinc.dummyprovider.provider.DummyMetadataItem;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.abstraction.transformer.PhysicalResourceTransformer;

/**
 *
 * @author hungld
 */
public class DummyProviderTransformer implements PhysicalResourceTransformer<DummyMetadataItem> {

    @Override
    public IoTUnit translateIoTUnit(DummyMetadataItem data) {
        IoTUnit unit = new IoTUnit();
        unit.getPhysicalResourceUuid().add(data.getId());
        if (data.getName() == null || data.getName().isEmpty()) {
            // the IoT Unit should be removed
            Map<String, String> meta = new HashMap<>();
            meta.put("remove", "true");
            unit.setState(meta);
            return unit;
        }
        DataPoint dp = new DataPoint(data.getName());
        dp.getMeta().put("type", data.getType());
        dp.getMeta().put("unit", data.getUnit());
        unit.hasDatapoint(dp);
        return unit;
    }

    @Override
    public String getName() {
        return "dummy";
    }

}
