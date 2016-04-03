/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.extensions;

import at.ac.tuwien.dsg.hinc.model.PhysicalResource.ExtensibleModel;

/**
 *
 * @author hungld
 */
public class SensorExtension extends ExtensibleModel{

    Integer rate;

    public SensorExtension() {
        super(SensorExtension.class);
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

}
