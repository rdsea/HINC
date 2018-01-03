/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.testrigprovider.plugin;

import java.util.UUID;
import sinc.hinc.abstraction.transformer.IoTUnitTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 *
 * @author linhsolar
 */
public class TestRigProviderTransformer implements IoTUnitTransformer<TestRigMetadataItem> {

    @Override
    public IoTUnit translateIoTUnit(TestRigMetadataItem data) {
        //we have to check the data carefully as the metadata describes 
        //different types of devices
        //here we just few
        IoTUnit unit = new IoTUnit();
        unit.setHincID(UUID.randomUUID().toString());
        unit.setResourceID(data.getId());
        if (data.getDistance() !=null) {
            DataPoint dp = new DataPoint();
            dp.setName("distance");
            dp.setDataApi(data.getDistance().get("url"));
            unit.hasDatapoint(dp);
        }
        return unit;
    }

    @Override
    public String getName() {
        return "testrig";
    }

}
