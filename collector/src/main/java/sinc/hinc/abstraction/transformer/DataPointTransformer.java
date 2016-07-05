/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.transformer;

import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;

/**
 *
 * @author hungld
 */
public interface DataPointTransformer<DomainModel> {

    /**
     * For each items, this extracts one data point
     *
     * @param data Information item captured from provider
     * @return A single data point.
     */
    public DataPoint updateDataPoint(DomainModel data);
    
    
    /**
     * The readable name of the Transformer
     * @return 
     */
    public String getName();

}
