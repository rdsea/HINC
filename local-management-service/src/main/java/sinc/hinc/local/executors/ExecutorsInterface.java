/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.executors;

import sinc.hinc.communication.payloads.ControlResult;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;

/**
 *
 * @author hungld
 */
public interface ExecutorsInterface {

    public ControlResult execute(ControlPoint controlPoint);


}
