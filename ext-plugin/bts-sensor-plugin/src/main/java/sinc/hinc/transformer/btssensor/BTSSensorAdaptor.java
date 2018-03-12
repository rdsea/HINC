package sinc.hinc.transformer.btssensor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.json.simple.JSONObject;
import sinc.hinc.abstraction.ResourceDriver.PluginDataRepository;
import sinc.hinc.abstraction.ResourceDriver.ProviderControlResult;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.transformer.btssensor.model.Sensor;
import sinc.hinc.transformer.btssensor.model.SensorDescription;
import sinc.hinc.transformer.btssensor.model.SensorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

import java.io.IOException;
import java.util.*;

public class BTSSensorAdaptor implements ProviderQueryAdaptor<SensorItem>{
    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public Collection<SensorItem> getItems(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();

        List<SensorDescription> sensorDescriptions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            logger.info("fetching sensor descriptions");
            String response = APIHandler.get(endpoint, "/sensor/bts");
            sensorDescriptions = Arrays.asList(mapper.readValue(response, SensorDescription[].class));
            logger.info("found "+sensorDescriptions.size()+" metadata entities");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<SensorItem> sensorItems = new ArrayList<SensorItem>();


        // go through each sensor endpoint and extract sensors
        logger.info("fetching sensors");

        for(SensorDescription description: sensorDescriptions){
            List<Sensor> sensors = new ArrayList<>();
            try {
                String response = APIHandler.get(endpoint, description.getUrl());
                sensors = Arrays.asList(mapper.readValue(response, Sensor[].class));
            } catch (Exception e) {
                e.printStackTrace();
            }

            SensorItem item = new SensorItem();
            item.setDescription(description);
            item.setSensors(sensors);
            sensorItems.add(item);
        }

        return sensorItems;
    }

    public ProviderControlResult sendControl(ControlPoint controlPoint) {
        ProviderControlResult result = new ProviderControlResult();
        JSONObject configuration = new JSONObject();
        Iterator keyIt = controlPoint.getParameters().keySet().iterator();
        while(keyIt.hasNext()){
            String key = (String) keyIt.next();
            configuration.put(key, controlPoint.getParameters().get(key));
        }
        try {
            String response = APIHandler.post(controlPoint.getReference(), configuration.toJSONString());
            result.setOutput(response);
            result.setResult(ProviderControlResult.CONTROL_RESULT.SUCCESS);
            // TODO create new iot unit, not that urgent local management service periodically scans anyway
            //result.setUpdateIoTUnit();
        } catch (IOException e) {
            result.setOutput(e.getMessage());
            result.setResult(ProviderControlResult.CONTROL_RESULT.COMMAND_EXIT_ERROR);
            logger.error("failed to send control");
            e.printStackTrace();
        }

        return result;
    }

    public ResourcesProvider getProviderAPI(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();
        ResourcesProvider provider = new ResourcesProvider();
        provider.setSettings(settings);
        provider.setName("bts sensor provider");
        provider.setUri(endpoint+"/sensor/bts");

        List<ControlPoint> controlPoints = new ArrayList<ControlPoint>();

        // we get the metadata
        logger.info("obtaining sensor descriptions");
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<SensorDescription> sensorDescriptions = new ArrayList<>();
        try {
            logger.info("fetching sensor descriptions");
            String response = APIHandler.get(endpoint, "/sensor/bts");
            sensorDescriptions = Arrays.asList(mapper.readValue(response, SensorDescription[].class));
            logger.info("found "+sensorDescriptions.size()+" metadata entities");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(SensorDescription description: sensorDescriptions){
            ControlPoint controlPoint = new ControlPoint();
            controlPoint.setControlType(ControlPoint.ControlType.PROVISION);
            controlPoint.setName(description.getName() + " sensor provider");
            controlPoint.setInvokeProtocol(ControlPoint.InvokeProtocol.POST);
            controlPoint.setReference(description.getUrl());
            controlPoint.setParameters(description.getSampleConfiguration().asMap());
            controlPoint.setIotUnitID(provider.getUri());
            controlPoints.add(controlPoint);
        }
        logger.info(controlPoints.size()+" apis obtained from "+provider.getName());
        provider.setApis(controlPoints);

        return provider;
    }

    public String getName() {
        return "bts-sensor";
    }

    @Override
    public void createResources(PluginDataRepository pluginDataRepository, Map<String, String> settings) {
        Collection<SensorItem> items = this.getItems(settings);
        List<IoTUnit> units = new ArrayList<>();
        logger.info("found "+units.size()+"IoTUnits from "+getName());
        for(SensorItem item: items){
            for(Sensor sensor: item.getSensors()){
                IoTUnit unit = new IoTUnit();
                unit.setResourceID(sensor.getClientId());

                DataPoint dp = new DataPoint();
                dp.setDatatype(item.getDescription().getMeasurement());
                dp.setConnectingTo(new AccessPoint(sensor.getUri()));
                dp.setMeasurementUnit(item.getDescription().getUnit());
                dp.setName(item.getDescription().getName());

                unit.hasDatapoint(dp);
                units.add(unit);
            }
        }

        pluginDataRepository.saveIoTUnits(units);

        ResourcesProvider provider = this.getProviderAPI(settings);
        List<ResourcesProvider> providers = new ArrayList<>();
        providers.add(provider);
        logger.info("found "+providers.size()+"resource providers from "+getName());
        pluginDataRepository.saveResourceProviders(providers);

        System.out.println("Random number with external dependency: "+RandomUtils.nextInt());

    }
}
