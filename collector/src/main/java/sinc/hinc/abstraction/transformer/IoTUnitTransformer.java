/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.transformer;

import sinc.hinc.model.VirtualComputingResource.VirtualResource;

/**
 * To transform some model into IoTUnit.
 * 
 * In general case, HINC will match the tranformer.name and adaptor.name
 * to process the information.
 * 
 * TODO: add 1 more interface here to support 1 adaptor, multiple transformer
 * @author hungld
 */
public interface IoTUnitTransformer<DomainModel> {

    /**
     * For each items, this extracts one data point
     *
     * @param data Information item captured from provider
     * @return A single data point.
     */
    public VirtualResource translateIoTUnit(DomainModel data);

    /**
     * The readable name of the Transformer
     * An adaptor only accepts a set of tranformer name.
     * @return
     */
    public String getName();

}
