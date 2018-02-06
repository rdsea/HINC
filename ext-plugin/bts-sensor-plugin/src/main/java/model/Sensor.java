package model;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sensor {
    String id;
    String type;
    String broker;
    String topic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    // parse json into sensors
    public static List<Sensor> getSensors(String body) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(body);

        List<Sensor> sensors = new ArrayList<Sensor>();
        for(Iterator it = jsonArray.iterator(); it.hasNext();){
            JSONObject jsonObject = (JSONObject) it.next();
            Sensor sensor = new Sensor();
            sensor.setBroker((String) jsonObject.get("broker"));
            sensor.setTopic((String) jsonObject.get("topic"));
            sensor.setId((String) jsonObject.get("clientId"));
            sensor.setType((String) jsonObject.get("type"));
            sensors.add(sensor);
        }
        return sensors;
    }
}
