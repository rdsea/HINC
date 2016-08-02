/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.slice;

import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualNetworkResource.NetworkService;

/**
 *
 * @author hungld
 */
public class AppSlice {

    DataPoint datapoint;
    NetworkService network;
    CloudService cloud;

    public AppSlice() {
    }

    public AppSlice(DataPoint datapoint, NetworkService network, CloudService cloud) {
        this.datapoint = datapoint;
        this.network = network;
        this.cloud = cloud;
    }

    public DataPoint getDatapoint() {
        return datapoint;
    }

    public void setDatapoint(DataPoint datapoint) {
        this.datapoint = datapoint;
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
