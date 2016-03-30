/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.abstraction.transformer;

import at.ac.tuwien.dsg.hinc.model.VirtualNetworkResource.VNF;

/**
 *
 * @author hungld
 * @param <ResourceDomainClass> Depending on the router
 */
public interface RouterResourceTranformationInterface<ResourceDomainClass> {

    public ResourceDomainClass validateAndConvertToDomainModel(String rawData);

    public VNF toVNF(ResourceDomainClass domainClass);
}
