/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.haivancameraprovider.provider;

import java.util.HashMap;
import java.util.Map;
import sinc.hinc.abstraction.transformer.IoTUnitTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 *
 * @author hungld
 */
public class CameraTransformer implements IoTUnitTransformer<CameraMetadataItem> {

    @Override
    public IoTUnit translateIoTUnit(CameraMetadataItem data) {
        IoTUnit unit = new IoTUnit();
        unit.setResourceID(data.getId());
        DataPoint dp = new DataPoint(data.getUrl(), "video");
        dp.setMeasurementUnit("NA");
        unit.hasDatapoint(dp);
        return unit;
    }

    @Override
    public String getName() {
        return "CameraProvider";
    }

}
