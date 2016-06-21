/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.Extensible.misc;

import sinc.hinc.model.Extensible.ExtensibleModel;

/**
 *
 * @author hungld
 */
public class SensorProps extends ExtensibleModel{

    Integer rate;

    public SensorProps() {
        super(SensorProps.class);
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

}
