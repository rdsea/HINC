package sinc.hinc.abstraction.ResourceDriver;

import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.SoftwareArtifact.MicroserviceArtifact;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;

import java.util.Collection;

/**
 * This data repository enables the plugins to use a service to interact with the
 * data services of the parent entity(i.e Local management service)
 * This way the plugins are responsible for fetching and transforming any data
 * from their respective providers
 *
 * @author lingfan gao
 */

public interface PluginDataRepository {
    public void saveIoTUnits(Collection<IoTUnit> units);

    public void saveNetworkFunctions(Collection<NetworkFunctionService> networkFunctionServices);

    public void saveCloudServices(Collection<CloudService> cloudServices);

    public void saveResourceProviders(Collection<ResourcesProvider> resourcesProviders);
}
