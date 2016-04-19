/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.communication.messagePayloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hungld
 */
public class HincMeta {

    String uuid;
    String ip;
    String unicastTopic;
    String settings;

    public HincMeta() {
    }

    public HincMeta(String uuid, String ip, String unicastTopic) {
        this.uuid = uuid;
        this.ip = ip;
        this.unicastTopic = unicastTopic;
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

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static HincMeta fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, HincMeta.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

}
