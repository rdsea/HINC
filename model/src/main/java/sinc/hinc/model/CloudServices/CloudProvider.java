/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.CloudServices;

import java.util.List;
import java.util.Map;

/**
 *
 * @author hungld
 */
public class CloudProvider {

    public enum ProviderType {
        IaaS, PaaS, DockerFarm
    }
    // to link inside HINC system
    String uuid;
    // the full name of the provider, e.g. "TU Wien OpenStack, HUST DockerFarm"
    String name;
    // the platform name, e.g. OpenStack, CloudStack
    String platform;
    // the version of the platform
    String version;
    // type of the provider, to better classicication
    ProviderType type;
    // the list of service that provider give, e.g. compute, storage, identify, etc...
    Map<String, String> serviceEndpoints;
    // the place of such provider
    DataCenter dataCenter;
    // the list of running instances, e.g. created VMs, volumes, UserID. This list should be large.
    List<CloudService> serviceInstance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ProviderType getType() {
        return type;
    }

    public void setType(ProviderType type) {
        this.type = type;
    }

    public Map<String, String> getServiceEndpoints() {
        return serviceEndpoints;
    }

    public void setServiceEndpoints(Map<String, String> serviceEndpoints) {
        this.serviceEndpoints = serviceEndpoints;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

}
