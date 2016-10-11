/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.ResourceDriver;

import java.util.Map;
import sinc.hinc.model.VirtualComputingResource.MicroService;

/**
 *
 * @author hungld
 */
public interface ServiceDetector {

    MicroService detect(Map<String, String> settings);

    String getName();
}
