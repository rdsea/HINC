/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.slice;

import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.VirtualResource;
import sinc.hinc.model.VirtualNetworkResource.NetworkService;

/**
 *
 * @author hungld
 */
public class AppSlice {

    VirtualResource iotUnit;
    NetworkService network;
    CloudService cloud;

    public AppSlice() {
    }

    public AppSlice(VirtualResource iotUnit, NetworkService network, CloudService cloud) {
        this.iotUnit = iotUnit;
        this.network = network;
        this.cloud = cloud;
    }

    public VirtualResource getIotUnit() {
        return iotUnit;
    }

    public void setIotUnit(VirtualResource iotUnit) {
        this.iotUnit = iotUnit;
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
