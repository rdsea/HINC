/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openhab;

import sinc.hinc.transformer.openhab.model.Item;
import sinc.hinc.abstraction.transformer.DataPointTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;

/**
 *
 * @author hungld
 */
public class TranformOpenHABInfo implements DataPointTransformer<Item> {

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

}
