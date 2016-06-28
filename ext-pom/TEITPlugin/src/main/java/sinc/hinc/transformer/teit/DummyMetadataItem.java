/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.teit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author hungld
 */
public class DummyMetadataItem {

    public enum BUFFER_TYPE {
        cmd, mqtt
    }

    String id;
    String name;
    String description;
    String type;
    String unit;
    BUFFER_TYPE bufferType;
    Map<String, String> bufferSettings;

    public static void main(String[] args) throws JsonProcessingException, IOException {
        // test dummy --> json
        DummyMetadataItem item = new DummyMetadataItem();
        Map<String, String> settings = new HashMap<>();
        settings.put("broker", "tcp://localhost:1883");
        settings.put("topic", "mysensor1234");
        item.setBufferType(BUFFER_TYPE.mqtt);
        item.setBufferSettings(settings);
        item.setId("test");
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(item));

    }

    public DummyMetadataItem() {
        this.id = UUID.randomUUID().toString();
    }

    public DummyMetadataItem(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BUFFER_TYPE getBufferType() {
        return bufferType;
    }

    public void setBufferType(BUFFER_TYPE bufferType) {
        this.bufferType = bufferType;
    }

    public Map<String, String> getBufferSettings() {
        return bufferSettings;
    }

    public void setBufferSettings(Map<String, String> bufferSettings) {
        this.bufferSettings = bufferSettings;
    }

}
