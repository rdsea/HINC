import sinc.hinc.transformer.btssensor.model.Sensor;
import sinc.hinc.transformer.btssensor.model.SensorMetadata;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class AdaptorUnitTests {

    @Test
    public void getMetadataParse(){
        String json = "{\"param\":{\"url\":\"/sensor/bts/param\",\"sampleConfiguration\":{\"broker\":\"127.0.0.1\",\"topic\":\"myTopic\"},\"communication\":[\"mqtt\"],\"measurement\":\"parameters\",\"unit\":\"xxx\"},\"alert\":{\"url\":\"/sensor/bts/alert\",\"sampleConfiguration\":{\"broker\":\"127.0.0.1\",\"topic\":\"myTopic\"},\"measurement\":\"alerts\",\"unit\":\"yyy\",\"communication\":[\"mqtt\"]}}";

        List<SensorMetadata> sensorMetadata = null;
        try {
            sensorMetadata = SensorMetadata.getMetaData(json);
        } catch (Exception e) {
            e.printStackTrace();
            fail("an exception occurred parsing the json string ");
        }

        assert(sensorMetadata != null);
        assert(sensorMetadata.size() == 2);

        assert(sensorMetadata.get(0).getType().equals("param"));
        assert(sensorMetadata.get(0).getCommunication().size() == 1);
        assert(sensorMetadata.get(0).getMeasurement().equals("parameters"));
        assert(sensorMetadata.get(0).getUnit().equals("xxx"));
        assert(sensorMetadata.get(0).getUrl().equals("/sensor/bts/param"));

        Map<String, String> configuration = sensorMetadata.get(0).getConfiguration();

        assert(((String) configuration.get("topic")).equals("myTopic"));
        assert(((String) configuration.get("broker")).equals("127.0.0.1"));
    }

    @Test public void getSensorParse(){
        String json = "[{\"_id\":\"5a72cf3d5f1c0b656b452cf5\",\"type\":\"PARAM\",\"clientId\":\"sensor1517473594352\",\"broker\":\"localhost\",\"topic\":\"test\",\"createdAt\":1517473597,\"__v\":0},{\"_id\":\"5a72d9da5f1c0b656b452cf6\",\"type\":\"PARAM\",\"clientId\":\"sensor1517476312899\",\"broker\":\"localhost\",\"topic\":\"test\",\"createdAt\":1517476314,\"__v\":0}]";
        List<Sensor> sensors = null;

        try{
            sensors = Sensor.getSensors(json);
        }catch(Exception e){
            e.printStackTrace();
            fail("an exception occured while parsing the json string");
        }

        assert(sensors != null);
        assert(sensors.size() == 2);

        assert(sensors.get(0).getBroker().equals("localhost"));
        assert(sensors.get(0).getId().equals("sensor1517473594352"));
        assert(sensors.get(0).getTopic().equals("test"));
        assert(sensors.get(0).getType().equals("PARAM"));
    }
}
