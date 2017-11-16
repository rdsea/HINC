/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.transformer;

import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 * Transform a physical resource to a Virtual Resource.
 * 
 * 1. An adaptor queries information of the physical resource.
 * 2. Match the tranformer.name and adaptor.name to find a transformer.
 * 3. Transformer (this class) will translate that data to Virtual IoT Resource.
 * 4. Result: a mapping 1-1 of Physical Resource and Virtual Resource
 * 
 * Clarification: This is a simple case to map a single Physical Resource to 
 *                a Virtual Resource. After that, users or applications can
 *                create more Virtual Resource based on their needs.
 * 
  * @author hungld
 */
public interface PhysicalResourceTransformer<DomainModel> {

    /**
     * Extract data points and control point.
     *
     * @param data Information item captured from provider
     * @return A single Virtual Resource
     */
    public IoTUnit translateIoTUnit(DomainModel data);

    /**
     * The readable name of the Transformer
     * An adaptor only accepts a set of tranformer name.
     * @return
     */
    public String getName();

}
