import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.IngestionClient;
import model.IngestionClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.abstraction.ResourceDriver.PluginDataRepository;
import sinc.hinc.abstraction.ResourceDriver.ProviderControlResult;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.SoftwareArtifact.MicroserviceArtifact;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

import java.io.IOException;
import java.util.*;

public class IngestionAdaptor implements ProviderQueryAdaptor<IngestionClient>{
    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    @Override
    public Collection<IngestionClient> getItems(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();

        List<IngestionClient> ingestionClients = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            logger.info("fetching ingestion clients");
            String response = APIHandler.get(endpoint, "/ingestionClient/list");
            ingestionClients = Arrays.asList(mapper.readValue(response, IngestionClient[].class));
            logger.info("found "+ingestionClients.size()+" ingestion clients");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ingestionClients;
    }

    public static void main(String[] args){
        Map<String, String> settings = new HashMap<>();
        settings.put("endpoint", "http://localhost:3000");

        IngestionAdaptor adaptor = new IngestionAdaptor();
        Collection<IngestionClient> ingestionClients = adaptor.getItems(settings);
        System.out.println(ingestionClients.size());
    }

    @Override
    public ProviderControlResult sendControl(ControlPoint controlPoint) {
        ProviderControlResult result = new ProviderControlResult();

        try{
            // TODO find a better way than take a generic string probably use a jackson node?
            String response = APIHandler.post(controlPoint.getReference(), controlPoint.getParameters().get("config"));
            result.setOutput(response);
            result.setResult(ProviderControlResult.CONTROL_RESULT.SUCCESS);
        }catch(Exception e){
            e.printStackTrace();
            result.setOutput(e.getMessage());
            result.setResult(ProviderControlResult.CONTROL_RESULT.COMMAND_EXIT_ERROR);
            logger.error("failed to send control");
        }

        return result;
    }

    @Override
    public ResourcesProvider getProviderAPI(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();
        ResourcesProvider provider = new ResourcesProvider();
        provider.setSettings(settings);
        provider.setName("bts ingestion client provider");
        provider.setUri(endpoint+"/ingestionClient");
        provider.setResourceType(MicroserviceArtifact.class);

        Map<String, String> params = new HashMap<>();
        IngestionClientConfig config = new IngestionClientConfig();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            logger.info("fetching ingestion client config");
            String response = APIHandler.get(endpoint, "/ingestionClient");
            config = mapper.readValue(response, IngestionClientConfig.class);
            params.put("config", mapper.writeValueAsString(config.getSampleConfiguration()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        ControlPoint controlPoint = new ControlPoint();
        controlPoint.setControlType(ControlPoint.ControlType.PROVISION);
        controlPoint.setName("bts ingestion client provider");
        controlPoint.setInvokeProtocol(ControlPoint.InvokeProtocol.POST);
        controlPoint.setReference(endpoint+"/ingestionClient");
        controlPoint.setParameters(params);
        controlPoint.setIotUnitID(provider.getUri());

        List<ControlPoint> controlPoints = new ArrayList<>();
        controlPoints.add(controlPoint);
        provider.setApis(controlPoints);
        return provider;

    }

    @Override
    public String getName() {
        return "bts-ingestion";
    }

    @Override
    public void createResources(PluginDataRepository pluginDataRepository, Map<String, String> settings) {
        Collection<IngestionClient> ingestionClients = this.getItems(settings);
        List<CloudService> cloudServices = new ArrayList<>();

        logger.info("creating bts ingestion client cloud services");
        for(IngestionClient ingestionClient: ingestionClients){
            CloudService cloudService = new CloudService();
            cloudService.setProviderUUID("bts ingestion client provider");
            cloudService.setHostedOnUUID("container");
            cloudService.setType("software unit");
            cloudService.setUuid(ingestionClient.getIngestionClientId());
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            Map<String, String> attributes = new HashMap<>();
            try {
                attributes.put("config", mapper.writeValueAsString(ingestionClient));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            cloudService.setAttributes(attributes);
            cloudServices.add(cloudService);
        }

        pluginDataRepository.saveCloudServices(cloudServices);

        logger.info("creating bts ingestion resource provider");
        List<ResourcesProvider> providers = new ArrayList<>();
        providers.add(this.getProviderAPI(settings));
        pluginDataRepository.saveResourceProviders(providers);
    }
}
