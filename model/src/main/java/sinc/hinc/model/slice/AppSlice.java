/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.slice;

import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;

/**
 *
 * @author hungld
 */
//linhsolar: This is just an extreme simple of application slice
public class AppSlice {

    IoTUnit iotUnit;
    NetworkFunctionService network;
    CloudService cloud;

    public AppSlice() {
    }

    public AppSlice(IoTUnit iotUnit, NetworkFunctionService network, CloudService cloud) {
        this.iotUnit = iotUnit;
        this.network = network;
        this.cloud = cloud;
    }

    public IoTUnit getIotUnit() {
        return iotUnit;
    }

    public void setIotUnit(IoTUnit iotUnit) {
        this.iotUnit = iotUnit;
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
