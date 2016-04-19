/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openhab;

import sinc.hinc.abstraction.transformer.GatewayResourceTransformationInterface;
import sinc.hinc.model.PhysicalResource.PhysicalResource;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import sinc.hinc.transformer.openhab.model.Item;
import java.util.List;

/**
 *
 * @author hungld
 */
public class TranformOpenHABInfo implements GatewayResourceTransformationInterface<Item> {

    @Override
    public Item validateAndConvertToDomainModel(String rawData, String dataSource) {
        Item item = new Item();
        return (Item)item.readFromJson(rawData);
    }

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
