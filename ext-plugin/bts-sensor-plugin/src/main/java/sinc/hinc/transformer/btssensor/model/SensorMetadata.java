package sinc.hinc.transformer.btssensor.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SensorMetadata {
    String type;
    String measurement;
    String unit;
    String configuration;
    List<String> communication;
    String url;

    public SensorMetadata(){
        this.communication = new ArrayList<String>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public List<String> getCommunication() {
        return communication;
    }

    public void setCommunication(List<String> communication) {
        this.communication = communication;
    }

    // returns a list of sensorMetadata from json string
    public static List<SensorMetadata> getMetaData(String body) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        JSONObject obj = (JSONObject) parser.parse(body);
        List<JSONObject> sensorTypes = new ArrayList<JSONObject>();

        // get each separate sensor type from response
        for(Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            ((JSONObject) obj.get(key)).put("type", key);
            sensorTypes.add((JSONObject) obj.get(key));
        }

        List<SensorMetadata> sensorTypeMetadata = new ArrayList<SensorMetadata>();
        for(JSONObject sensorType: sensorTypes){
            SensorMetadata metadata = new SensorMetadata();
            metadata.setMeasurement((String) sensorType.get("measurement"));
            metadata.setUnit((String) sensorType.get("unit"));
            metadata.setType((String) sensorType.get("type"));
            metadata.setUrl((String) sensorType.get("url"));
            metadata.setConfiguration(((JSONObject) sensorType.get("sampleConfiguration")).toJSONString());

            JSONArray communication = (JSONArray) sensorType.get("communication");
            Iterator i = communication.iterator();

            while (i.hasNext()) {
                metadata.getCommunication().add((String) i.next());
            }
            sensorTypeMetadata.add(metadata);
        }

        return sensorTypeMetadata;
    }
}
