/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.slice;

import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.model.VirtualNetworkResource.NetworkService;

/**
 *
 * @author hungld
 */
public class InfrastructureSlice {

    IoTUnit gateway;
    NetworkService network;
    CloudService cloud;

    public IoTUnit getGateway() {
        return gateway;
    }

    public void setGateway(IoTUnit gateway) {
        this.gateway = gateway;
    }

    public NetworkService getNetwork() {
        return network;
    }

    public void setNetwork(NetworkService network) {
        this.network = network;
    }

    public CloudService getCloud() {
        return cloud;
    }

    public void setCloud(CloudService cloud) {
        this.cloud = cloud;
    }

}
