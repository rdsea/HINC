/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.common.metadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hungld
 */
public class HincLocalMeta {

    String uuid;
    String ip;
    String unicastTopic;
    String settings;
    String groupName;
    String broker;
    String brokerType;
    // some location meta
    String city;
    String country;
    String isp;
    Double lat;
    Double lon;
    // list of listeners
    Map<String, Map<String, String>> handlers;
    // list of adaptors for provider
    Map<String, String> adaptor;
    

    public HincLocalMeta() {

    }

    public HincLocalMeta(String uuid, String ip, String unicastTopic) {
        this.uuid = uuid;
        this.ip = ip;
        this.unicastTopic = unicastTopic;
    }

    public void hasHandler(String topic, String messageType, String clazz) {
        if (handlers == null) {
            handlers = new HashMap<>();
        }
        Map<String, String> map = handlers.get(topic);
        if (map == null) {
            map = new HashMap<>();
            map.put(messageType, clazz);
            handlers.put(topic, map);
        } else {
            map.put(messageType, clazz);
        }
    }

    public String getUnicastTopic() {
        return unicastTopic;
    }

    public void setUnicastTopic(String unicastTopic) {
        this.unicastTopic = unicastTopic;
    }

    public String getUuid() {
        return uuid;
    }

    public String getIp() {
        return ip;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getBrokerType() {
        return brokerType;
    }

    public void setBrokerType(String brokerType) {
        this.brokerType = brokerType;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public static HincLocalMeta fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, HincLocalMeta.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public Map<String, Map<String, String>> getHandlers() {
        return handlers;
    }

}
