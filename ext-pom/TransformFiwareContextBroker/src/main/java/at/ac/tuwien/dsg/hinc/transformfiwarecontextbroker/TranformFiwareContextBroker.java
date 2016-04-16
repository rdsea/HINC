/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.transformfiwarecontextbroker;

import at.ac.tuwien.dsg.hinc.abstraction.transformer.GatewayResourceTransformationInterface;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import at.ac.tuwien.dsg.hinc.transformfiwarecontextbroker.model.ContextElement;
import java.util.List;

/**
 *
 * @author hungld
 */
public class TranformFiwareContextBroker implements GatewayResourceTransformationInterface<ContextElement>{

    @Override
    public ContextElement validateAndConvertToDomainModel(String rawData, String dataSource) {
        return ContextElement.fromJson(rawData);
    }

    @Override
    public DataPoint updateDataPoint(ContextElement data) {
        DataPoint dp = new DataPoint();
        dp.setResourceID(data.getId());
        dp.setName(data.getAttributes().get(0).getName());
        dp.setDatatype(data.getAttributes().get(0).getType());
        return dp;
    }

    @Override
    public List<ControlPoint> updateControlPoint(ContextElement data) {
        return null;
    }

    @Override
    public ExecutionEnvironment updateExecutionEnvironment(ContextElement data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloudConnectivity updateCloudConnectivity(ContextElement data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
