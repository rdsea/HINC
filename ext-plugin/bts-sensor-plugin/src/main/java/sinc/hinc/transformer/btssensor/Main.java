package sinc.hinc.transformer.btssensor;

import sinc.hinc.transformer.btssensor.model.SensorItem;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

import java.util.*;

public class Main {
    public static void main(String[] args){
        getSensors();
    }

    // change this depending on where the provider endpoing is running
    public static final String ENDPOINT = "http://localhost:3000";

    public static void getSensors(){
        Map<String, String> settings = new HashMap<String, String>();
        settings.put("endpoint", ENDPOINT);

        BTSSensorAdaptor adaptor = new BTSSensorAdaptor();
        Collection<SensorItem> items = adaptor.getItems(settings);

        List<IoTUnit> units = new ArrayList<IoTUnit>();

        SensorTransform transformer = new SensorTransform();
        for(SensorItem item: items){
            units.add(transformer.translateIoTUnit(item));
        }

        System.out.println(units.size());
    }
}
