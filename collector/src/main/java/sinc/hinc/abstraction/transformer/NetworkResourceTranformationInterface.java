/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.transformer;

import sinc.hinc.model.VirtualNetworkResource.VNF;

/**
 *
 * @author hungld
 * @param <ResourceDomainClass> Depending on the router
 */
public interface NetworkResourceTranformationInterface<ResourceDomainClass> {

    public ResourceDomainClass validateAndConvertToDomainModel(String rawData);

    public VNF toVNF(ResourceDomainClass domainClass);
}
