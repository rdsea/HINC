/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openhab;

import sinc.hinc.model.PhysicalResource.PhysicalResource;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import sinc.hinc.transformer.openhab.model.Item;
import java.util.List;
import sinc.hinc.abstraction.transformer.IoTResourceTransformation;

/**
 *
 * @author hungld
 */
public class TranformOpenHABInfo implements IoTResourceTransformation<Item> {


    @Override
    public DataPoint updateDataPoint(Item data) {
        DataPoint dp = new DataPoint();
        dp.setName(data.getName());
        dp.setDatatype(data.getType());        
        dp.setLink(data.getLink());
        return dp;
    }

    @Override
    public List<ControlPoint> updateControlPoint(Item data) {
        return null;
    }

    @Override
    public ExecutionEnvironment updateExecutionEnvironment(Item data) {
        return null;
    }

    @Override
    public CloudConnectivity updateCloudConnectivity(Item data) {
        return null;
    }

}
