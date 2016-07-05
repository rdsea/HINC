/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.transformer;

import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;

/**
 *
 * @author hungld
 */
public interface ConnectivityTransformater<DomainModel> {

    /**
     * How the data can be routed via different network.
     *
     * @param data Information item captured from provider
     * @return The description of connectivity that the provider supports for
     * such item. E.g. the data of this provider can forward to different
     * network.
     */
    public CloudConnectivity updateCloudConnectivity(DomainModel data);

}
