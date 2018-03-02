package sinc.hinc.local.plugin;

import sinc.hinc.abstraction.ResourceDriver.PluginDataRepository;
import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;
import sinc.hinc.repository.DAO.orientDB.IoTUnitDAO;

import java.util.Collection;

public class ProviderDataRepository implements PluginDataRepository {
    @Override
    public void saveIoTUnits(Collection<IoTUnit> units) {
        IoTUnitDAO ioTUnitDAO = new IoTUnitDAO();
        ioTUnitDAO.saveAll(units);
    }

    @Override
    public void saveNetworkFunctions(Collection<NetworkFunctionService> networkFunctionServices) {
        AbstractDAO networkFunctionDAO = new AbstractDAO(NetworkFunctionService.class);
        networkFunctionDAO.saveAll(networkFunctionServices);
    }

    @Override
    public void saveCloudServices(Collection<CloudService> cloudServices) {
        AbstractDAO cloudServicesDAO = new AbstractDAO(CloudService.class);
        cloudServicesDAO.saveAll(cloudServices);
    }

    @Override
    public void saveResourceProviders(Collection<ResourcesProvider> resourcesProviders) {
        AbstractDAO resourceProvider = new AbstractDAO(ResourcesProvider.class);
        resourceProvider.saveAll(resourcesProviders);
    }
}
