/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.fiwarecontextbroker;

import sinc.hinc.transformer.fiwarecontextbroker.model.ContextElement;
import java.util.List;
import sinc.hinc.abstraction.transformer.IoTResourceTransformation;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ExecutionEnvironment;

/**
 *
 * @author hungld
 */
public class TranformFiwareContextBroker implements IoTResourceTransformation<ContextElement> {

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
