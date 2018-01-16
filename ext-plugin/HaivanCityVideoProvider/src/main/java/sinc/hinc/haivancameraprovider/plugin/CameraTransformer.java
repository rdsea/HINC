/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.haivancameraprovider.plugin;

import java.util.HashMap;
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
        //let us get two data points. the first one is about all possible video
        DataPoint dp_all = new DataPoint(camera.getDatapoint(), "text");
        DataPoint dp_latest = new DataPoint((String)(camera.getMetadata().get("haivancameraprovider"))+"/camera/"+camera.getId()+"/list/now",camera.getType());
        dp_all.setMeasurementUnit("NA");
        unit.hasDatapoint(dp_all);
        dp_latest.setMeasurementUnit("NA");
        unit.hasDatapoint(dp_latest);
        HashMap<String,String> metadata = new HashMap();
        metadata.put("address", camera.getAddress());
        metadata.put("description", camera.getDescription());
        unit.setMeta(metadata);
        return unit;
    }

    @Override
    public String getName() {
        return "haivancamera";
    }

}
