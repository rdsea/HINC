/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.transformopenhab;

import at.ac.tuwien.dsg.hinc.abstraction.transformer.GatewayResourceTransformationInterface;
import at.ac.tuwien.dsg.hinc.model.PhysicalResource.PhysicalResource;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import at.ac.tuwien.dsg.hinc.transformopenhab.model.Item;
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
