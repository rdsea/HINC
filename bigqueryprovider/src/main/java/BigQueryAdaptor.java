import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Dataset;
import model.DatasetConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.abstraction.ResourceDriver.PluginDataRepository;
import sinc.hinc.abstraction.ResourceDriver.ProviderControlResult;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

import java.io.IOException;
import java.util.*;


public class BigQueryAdaptor implements ProviderQueryAdaptor<Dataset> {
    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Override
    public Collection<Dataset> getItems(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();

        List<Dataset> datasets = new ArrayList<>();
        ObjectMapper mapper =  new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            logger.info("fetching sensor descriptions");
            String response = APIHandler.get(endpoint, "/storage/bigquery/list");
            datasets = Arrays.asList(mapper.readValue(response, Dataset[].class));
            logger.info("found "+datasets.size()+" metadata entities");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datasets;
    }

    public static void main(String[] args){
        BigQueryAdaptor adaptor = new BigQueryAdaptor();

        Map<String, String> settings = new HashMap<>();
        settings.put("endpoint", "http://localhost:3000");
        Collection<Dataset> datasets = adaptor.getItems(settings);
        ResourcesProvider provider = adaptor.getProviderAPI(settings);

        System.out.println(datasets.size());

    }

    @Override
    public ProviderControlResult sendControl(ControlPoint controlPoint) {
        ProviderControlResult result = new ProviderControlResult();

        try{
            // TODO find a better way than take a generic string probably use a jackson node?
            String response = APIHandler.post(controlPoint.getReference(), controlPoint.getParameters().get("config"));
            result.setOutput(response);
            result.setResult(ProviderControlResult.CONTROL_RESULT.SUCCESS);
        }catch(IOException e){
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
        provider.setName("big query dataset provider");
        provider.setUri(endpoint+"/storage/bigquery");
        provider.setResourceType(CloudService.class);

        List<ControlPoint> controlPoints = new ArrayList<ControlPoint>();

        ControlPoint controlPoint = new ControlPoint();
        controlPoint.setControlType(ControlPoint.ControlType.PROVISION);
        controlPoint.setName("big query dataset provider");
        controlPoint.setInvokeProtocol(ControlPoint.InvokeProtocol.POST);
        controlPoint.setReference(endpoint+"/storage/bigquery");
        controlPoint.setIotUnitID(provider.getUri());

        DatasetConfig config = new DatasetConfig();
        ObjectMapper mapper =  new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Map<String, String> sampleConfig = new HashMap<>();

        try {
            logger.info("fetching sensor descriptions");
            String response = APIHandler.get(endpoint, "/storage/bigquery/");
            config = mapper.readValue(response, DatasetConfig.class);
            // TODO find a better way of representing this than in a single entry in a map!
            sampleConfig.put("config", mapper.writeValueAsString(config.getSampleConfig()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        controlPoint.setParameters(sampleConfig);
        controlPoints.add(controlPoint);
        provider.setApis(controlPoints);

        return provider;
    }

    @Override
    public String getName() {
        return "big-query";
    }

    @Override
    public void createResources(PluginDataRepository pluginDataRepository, Map<String, String> settings) {
        Collection<Dataset> datasets = this.getItems(settings);

        ObjectMapper mapper =  new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<CloudService> cloudServices = new ArrayList<>();
        for(Dataset dataset: datasets){
            CloudService cloudService = new CloudService();
            cloudService.setProviderUUID("google BigQuery");
            cloudService.setType("Cloud Storage");
            cloudService.setUuid(dataset.getDatasetId());

            Map<String, String> attributes = new HashMap<>();
            attributes.put("datasetId", dataset.getDatasetId());
            try {
                attributes.put("config", mapper.writeValueAsString(dataset));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            cloudService.setAttributes(attributes);
            cloudServices.add(cloudService);
        }

        pluginDataRepository.saveCloudServices(cloudServices);

        List<ResourcesProvider> resourcesProviders = new ArrayList<>();
        resourcesProviders.add(this.getProviderAPI(settings));
        pluginDataRepository.saveResourceProviders(resourcesProviders);
    }
}
