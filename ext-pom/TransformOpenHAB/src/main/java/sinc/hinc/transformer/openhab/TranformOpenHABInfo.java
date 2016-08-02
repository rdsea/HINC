/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openhab;

import java.util.List;
import sinc.hinc.abstraction.transformer.ControlPointTransformer;
import sinc.hinc.transformer.openhab.model.Item;
import sinc.hinc.abstraction.transformer.DataPointTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;

/**
 *
 * @author hungld
 */
public class TranformOpenHABInfo implements DataPointTransformer<Item>, ControlPointTransformer<Item> {

    @Override
    public DataPoint updateDataPoint(Item data) {
        DataPoint dp = new DataPoint();
        dp.setName(data.getName());
        dp.setDatatype(data.getType());
        return dp;
    }

    @Override
    public String getName() {
        return "openhab";
    }

    @Override
    public List<ControlPoint> updateControlPoint(Item data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
