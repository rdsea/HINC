/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.fiwarecontextbroker;

import sinc.hinc.transformer.fiwarecontextbroker.model.ContextElement;
import sinc.hinc.abstraction.transformer.DataPointTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;

/**
 *
 * @author hungld
 */
public class TranformFiwareContextBroker implements DataPointTransformer<ContextElement> {

    @Override
    public DataPoint updateDataPoint(ContextElement data) {
        DataPoint dp = new DataPoint();
        dp.setResourceID(data.getId());
        dp.setName(data.getAttributes().get(0).getName());
        dp.setDatatype(data.getAttributes().get(0).getType());
        return dp;
    }

    @Override
    public String getName() {
        return "fiwarecontextbroker";
    }

}
