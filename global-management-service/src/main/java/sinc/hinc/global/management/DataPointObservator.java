/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.management;

import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;



/**
 * This
 *
 * @author hungld
 */
public abstract class DataPointObservator {

    DataPoint observeMe;

    public DataPointObservator(DataPoint observeMe) {
        this.observeMe = observeMe;
    }

    public abstract void onChange(DataPoint newValue);

}
