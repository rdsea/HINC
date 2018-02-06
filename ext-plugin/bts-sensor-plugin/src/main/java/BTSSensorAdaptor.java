import model.Sensor;
import model.SensorItem;
import model.SensorMetadata;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BTSSensorAdaptor implements ProviderQueryAdaptor<SensorItem>{
    public Collection<SensorItem> getItems(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();

        List<SensorItem> sensorItems = new ArrayList<SensorItem>();

        // first we get the metadata
        List<SensorMetadata> sensorMetadata = new ArrayList<SensorMetadata>();

        try {
            sensorMetadata = SensorMetadata.getMetaData(APIHandler.get(endpoint,"/sensor/bts"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // go through each sensor endpoint and extract sensors
        for(SensorMetadata metadata: sensorMetadata){
            List<Sensor> sensors = new ArrayList<Sensor>();
            try {
                sensors = Sensor.getSensors(APIHandler.get(endpoint, metadata.getUrl()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            for(Sensor sensor: sensors){
                SensorItem item = new SensorItem();
                item.setMetadata(metadata);
                item.setSensor(sensor);
                sensorItems.add(item);
            }
        }

        return sensorItems;
    }

    public void sendControl(String s, Map<String, String> settings) {

    }

    public ResourcesProvider getProviderAPI(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();
        ResourcesProvider provider = new ResourcesProvider();
        provider.setSettings(settings);
        provider.setName("bts sensor provider");
        provider.setUri(endpoint+"/sensor/bts");

        List<ControlPoint> controlPoints = new ArrayList<ControlPoint>();

        // we get the metadata
        List<SensorMetadata> sensorMetadata = new ArrayList<SensorMetadata>();

        try {
            sensorMetadata = SensorMetadata.getMetaData(APIHandler.get(endpoint,"/sensor/bts"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // go through each sensor type and grab it's configuration
        for(SensorMetadata metadata: sensorMetadata){
            ControlPoint controlPoint = new ControlPoint();
            controlPoint.setControlType(ControlPoint.ControlType.PROVISION);
            controlPoint.setName(metadata.getType() + " sensor provider");
            controlPoint.setInvokeProtocol(ControlPoint.InvokeProtocol.POST);

            controlPoint.setParameters(metadata.getConfiguration());
            controlPoints.add(controlPoint);
        }


        return provider;
    }

    public String getName() {
        return null;
    }
}
