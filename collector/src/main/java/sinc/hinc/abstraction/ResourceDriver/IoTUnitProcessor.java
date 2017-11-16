/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.ResourceDriver;

import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 * The processor of IoT Unit is implemented by HINC Local, not by adaptor. This
 * is the input for the ProviderListenerAdaptor.
 *
 * @author hungld
 */
public interface IoTUnitProcessor {

    /**
     * How the HINC Local process the IoT unit when receiving one
     *
     * @param unit
     */
    public void process(IoTUnit unit);
}
