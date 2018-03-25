import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Broker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.abstraction.ResourceDriver.PluginDataRepository;
import sinc.hinc.abstraction.ResourceDriver.ProviderControlResult;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;

import java.io.IOException;
import java.util.*;

public class MosquittoAdaptor  implements ProviderQueryAdaptor<Broker> {
    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    @Override
    public Collection<Broker> getItems(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();

        List<Broker> brokers = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            logger.info("fetching mqtt brokers");
            String response = APIHandler.get(endpoint, "/mosquittobroker/list");
            brokers = Arrays.asList(mapper.readValue(response, Broker[].class));
            logger.info("found "+brokers.size()+" brokers");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brokers;
    }


    @Override
    public ProviderControlResult sendControl(ControlPoint controlPoint) {
        ProviderControlResult result = new ProviderControlResult();

        try {
            String response = APIHandler.post(controlPoint.getReference(), "");
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            Broker broker =  mapper.readValue(response, Broker.class);
            String location = broker.getLocation();
            while(location.contains("<pending>") || location.contains("creating")){
                response = APIHandler.get(controlPoint.getReference()+"/"+broker.getBrokerId());
                Broker[] brokers = mapper.readValue(response, Broker[].class);
                broker = brokers[0];
                location = broker.getLocation();
                Thread.sleep(3000);
                logger.info("broker location still not set, querying again in 3 seconds");
            }

            result.setOutput(response);
            result.setResult(ProviderControlResult.CONTROL_RESULT.SUCCESS);

            // TODO create new network function service, not that urgent local management service periodically scans anyway
            //result.setUpdateIoTUnit();
        } catch (IOException e) {
            result.setOutput(e.getMessage());
            result.setResult(ProviderControlResult.CONTROL_RESULT.COMMAND_EXIT_ERROR);
            logger.error("failed to send control");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ResourcesProvider getProviderAPI(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();
        ResourcesProvider provider = new ResourcesProvider();
        provider.setSettings(settings);
        provider.setName("mosquitto broker provider");
        provider.setUri(endpoint+"/mosquittobroker");
        provider.setResourceType(NetworkFunctionService.class);

        ControlPoint controlPoint = new ControlPoint();
        controlPoint.setControlType(ControlPoint.ControlType.PROVISION);
        controlPoint.setName("mosquitto broker provider");
        controlPoint.setInvokeProtocol(ControlPoint.InvokeProtocol.POST);
        controlPoint.setReference(endpoint+"/mosquittobroker");
        controlPoint.setParameters(new HashMap<String, String>());
        controlPoint.setIotUnitID(provider.getUri());

        List<ControlPoint> controlPoints = new ArrayList<>();
        controlPoints.add(controlPoint);
        provider.setApis(controlPoints);
        return provider;
    }

    @Override
    public String getName() {
        return "mosquitto-broker";
    }

    @Override
    public void createResources(PluginDataRepository pluginDataRepository, Map<String, String> settings) {
        Collection<Broker> brokers = this.getItems(settings);
        List<NetworkFunctionService> networkFunctionServices = new ArrayList<>();
        for(Broker broker: brokers){
            // create a new network function service
            NetworkFunctionService networkFunctionService = new NetworkFunctionService();
            networkFunctionService.setType(NetworkFunctionService.NetworkServiceType.BROKER_MQTT);
            networkFunctionService.setName(broker.getBrokerId());
            networkFunctionService.setUuid(broker.getBrokerId());

            AccessPoint accessPoint = new AccessPoint();
            accessPoint.setEndpoint(broker.getLocation());
            networkFunctionService.setAccessPoint(accessPoint);

            networkFunctionServices.add(networkFunctionService);
        }

        logger.info("found "+networkFunctionServices.size()+"network functions from "+getName());
        pluginDataRepository.saveNetworkFunctions(networkFunctionServices);

        ResourcesProvider provider = this.getProviderAPI(settings);
        List<ResourcesProvider> providers = new ArrayList<>();
        providers.add(provider);

        logger.info("found "+providers.size()+"resource providers from "+getName());
        pluginDataRepository.saveResourceProviders(providers);

    }
}
