/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.slice;

import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;

/**
 *
 * @author hungld
 */
//linhsolar: this is just an extremely simple of infrastructure slice: only gateway, network and cloud
public class InfrastructureSlice {

    SoftwareDefinedGateway gateway;
    NetworkFunctionService network;
    CloudService cloud;

    public SoftwareDefinedGateway getGateway() {
        return gateway;
    }

    public void setGateway(SoftwareDefinedGateway gateway) {
        this.gateway = gateway;
    }

    public NetworkFunctionService getNetwork() {
        return network;
    }

    public void setNetwork(NetworkFunctionService network) {
        this.network = network;
    }

    public CloudService getCloud() {
        return cloud;
    }

    public void setCloud(CloudService cloud) {
        this.cloud = cloud;
    }

}