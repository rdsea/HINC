/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource.ManagementInterface;

import java.util.Map;

/**
 * This interface to implement DataPoint management plugin
 * @author hungld
 */
public class DataPointManagement {
    // Name must be unique, so HINC can load 
    String name;
    Map<String, String> settings;
    
}
