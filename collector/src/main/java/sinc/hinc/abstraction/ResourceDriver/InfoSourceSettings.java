/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.ResourceDriver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hungld
 */
public class InfoSourceSettings {

    public static enum InformationSourceType {
        // FILE type scan all the files in one folder, recursively
        FILE,
        // call the REST endpoint
        REST,
        // run system command and get result
        SYSCMD,
        // some specific providers
        FiWARE, OpenHAB, IoTivity, WeaveRouter,
        MiniNet,
        OpenStack
    }

    // by default
    static final String DEFAULT_CONFIG_FILE = "./info-source.conf";
    List<InfoSource> source = new ArrayList<>();

    public InfoSourceSettings() {
    }

    public List<InfoSource> getSource() {
        return source;
    }

    public void setSource(List<InfoSource> source) {
        this.source = source;
    }

    public static class InfoSource {

        InformationSourceType type;
        String adaptorClass;
        String transformerClass;
        Map<String, String> settings = new HashMap<>();

        public InfoSource() {
        }

        public InfoSource(InformationSourceType type, String adaptorClass, String transformerClass) {
            this.type = type;
            this.adaptorClass = adaptorClass;
            this.transformerClass = transformerClass;
        }

        public InformationSourceType getType() {
            return type;
        }

        public void setType(InformationSourceType type) {
            this.type = type;
        }

//        public String getEndpoint() {
//            return endpoint;
//        }
//
//        public void setEndpoint(String endpoint) {
//            this.endpoint = endpoint;
//        }
        public String getTransformerClass() {
            return transformerClass;
        }

        public void setTransformerClass(String transformerClass) {
            this.transformerClass = transformerClass;
        }

        public String getAdaptorClass() {
            return adaptorClass;
        }

        public void setAdaptorClass(String adaptorClass) {
            this.adaptorClass = adaptorClass;
        }

        public Map<String, String> getSettings() {
            return settings;
        }

        public void setSettings(Map<String, String> settings) {
            this.settings = settings;
        }

        public InfoSource hasSetting(String key, String value) {
            this.settings.put(key, value);
            return this;
        }

        // below functions are hack for the simplicity
        @JsonIgnore
        public boolean isVNFResource() {
            String[] values = new String[]{"WeaveRouterResourceDiscovery"};
            String tranformClassName = transformerClass.substring(transformerClass.lastIndexOf(".") + 1);
            return Arrays.asList(values).contains(tranformClassName);
        }

        @JsonIgnore
        public boolean isGatewayResource() {
            String[] values = new String[]{"SDSensorTranformer", "AndroidSensorTransformer", "OpenIoTSensorTransformer"};
            String tranformClassName = transformerClass.substring(transformerClass.lastIndexOf(".") + 1);
            return Arrays.asList(values).contains(tranformClassName);
        }

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

    public static InfoSourceSettings fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, InfoSourceSettings.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static InfoSourceSettings loadFile(String file) {
        String json;
        try {
            json = new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return fromJson(json);
    }

    public static InfoSourceSettings loadDefaultFile() {
        return InfoSourceSettings.loadFile(DEFAULT_CONFIG_FILE);
    }

    public static void main(String[] args) {
        InfoSourceSettings settings = new InfoSourceSettings();
//        settings.getSource().add(new InfoSource(InformationSourceType.FILE, "/home/hungld/test/SENSOR_TEST/android.sensor", "at.ac.tuwien.dsg.cloud.salsa.informationmanagement.androidsensortranform.AndroidSensorTransformer", null));
//        settings.getSource().add(new InfoSource(InformationSourceType.FILE, "/home/hungld/test/SENSOR_TEST/OpenIoT", "at.ac.tuwien.dsg.cloud.salsa.informationmanagement.transformopeniotsensor.transformer.OpenIoTSensorTransformer", null));
//        settings.getSource().add(new InfoSource(InformationSourceType.FILE, "/opt/iCOMOT/bin/compact/workspace/iCOMOT-Platform/", "at.ac.tuwien.dsg.cloud.salsa.informationmanagement.tranformsdsensor.SDSensorTranformer", "sensor.meta"));
        InfoSource info1 = new InfoSource(InformationSourceType.OpenHAB, "at.ac..adapterClass1", "at.ac...tranformclass1");
        info1.hasSetting("endpoint", "http://anURL");
        InfoSource info2 = new InfoSource(InformationSourceType.FILE, "at.ac..adapterClass1", "at.ac...tranformclass1");
        info1.hasSetting("folder", "/tmp");
        
        settings.getSource().add(info1);
        settings.getSource().add(info2);
        System.out.println(settings.toJson());
    }

}
