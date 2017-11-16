/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openhab;

import sinc.hinc.transformer.openhab.model.Item;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.abstraction.transformer.PhysicalResourceTransformer;

/**
 *
 * @author hungld
 */
public class TranformOpenHABInfo implements PhysicalResourceTransformer<Item> {

    @Override
    public IoTUnit translateIoTUnit(Item data) {
        IoTUnit unit = new IoTUnit();
        unit.setResourceID(data.getName());
        
        DataPoint dp = new DataPoint();
        dp.setDatatype(data.getType());
        dp.setConnectingTo(new AccessPoint("unknown"));
        dp.setMeasurementUnit("unknown");
        
        unit.hasDatapoint(dp);
        return unit;
    }

    @Override
    public String getName() {
        return "openhab";
    }

   

}
