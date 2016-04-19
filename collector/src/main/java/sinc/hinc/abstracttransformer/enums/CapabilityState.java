/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstracttransformer.enums;

/**
 * This contains status of datapoint, controlpoint. E.g. when low level sensor is deactivated, or remove, it affect to datapoint
 * This is part of the CapabilityStatus
 * @author hungld
 */
public enum CapabilityState {
    ONLINE,
    OFFLINE,
    REMOVED
}
