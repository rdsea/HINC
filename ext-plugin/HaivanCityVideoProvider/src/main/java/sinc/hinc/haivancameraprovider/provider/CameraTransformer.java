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
 * @author linhsolar
 */
public class CameraTransformer implements IoTUnitTransformer<CameraMetadataItem> {

    @Override
    public IoTUnit translateIoTUnit(CameraMetadataItem camera) {
        IoTUnit unit = new IoTUnit();
        unit.setResourceID(camera.getId());
        DataPoint dp = new DataPoint(camera.getDatapoint(), camera.getType());
        dp.setMeasurementUnit("NA");
        unit.hasDatapoint(dp);
        HashMap<String,String> metadata = new HashMap();
        metadata.put("address", camera.getAddress());
        metadata.put("description", camera.getDescription());
        unit.setMeta(metadata);
        return unit;
    }

    @Override
    public String getName() {
        return "CameraProvider";
    }

}
