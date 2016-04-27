/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.transformer;

import sinc.hinc.model.CloudServices.CloudService;

/**
 * This interface is used to implement transformation process from cloud model to HINC cloud service model
 * @author hungld
 */
public interface CloudResourceTransformation<ResourceDomainClass> {
     public ResourceDomainClass validateAndConvertToDomainModel(String rawData);

    public CloudService toCloudService(ResourceDomainClass domainClass);
}
