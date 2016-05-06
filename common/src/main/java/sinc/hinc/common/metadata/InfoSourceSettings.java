/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.common.metadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class define the configuration about providers for HINC. It simply contains a list of InforSource, each for defining one provider.
 *
 * @author hungld
 */
public class InfoSourceSettings {

    // the configuration file by default
    static final String DEFAULT_CONFIG_FILE = "./info-source.conf";

    // the list of provider configuration
    List<InfoSource> source = new ArrayList<>();

    public InfoSourceSettings() {
    }

    public List<InfoSource> getSource() {
        return source;
    }

    public void setSource(List<InfoSource> source) {
        this.source = source;
    }

    public enum ProviderType {
        IoT, Network, Cloud
    }

    /**
     * Define a provider, which HINC then can manage via adaptor and transformer classes. The interval define the query rate that HINC will invoke provider to
     * get updated information. Interval can be:
     * <p>
     * <code>interval = 0</code> : Subscription. HINC call the provider one time. The next call is not effect.
     * <p>
     * <code>interval > 0</code> : Interval query. HINC call the provider after a number of seconds.
     * <p>
     * <code>interval < 0</code> : On-request. HINC call the provider whenever user request.
     */
    public static class InfoSource {

        String name;
        ProviderType type;
        int interval = -1; // by default, HINC will query provider one request        
        String adaptorClass;
        String transformerClass;
        Map<String, String> settings = new HashMap<>();

        public InfoSource() {
        }

        public InfoSource(String name, ProviderType type, int interval, String adaptorClass, String transformerClass) {
            this.name = name;
            this.type = type;
            this.interval = interval;
            this.adaptorClass = adaptorClass;
            this.transformerClass = transformerClass;
        }

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

        public InfoSource hasSetting(String key, String value) {
            this.settings.put(key, value);
            return this;
        }

        public String getName() {
            return name;
        }

        public int getInterval() {
            return interval;
        }

        public ProviderType getType() {
            return type;
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

    // test output
    public static void main(String[] args) {
        InfoSourceSettings settings = new InfoSourceSettings();
        InfoSource info1 = new InfoSource(null, ProviderType.IoT, 0, "at.ac..adapterClass1", "at.ac...tranformclass1");
        info1.hasSetting("endpoint", "http://anURL");
        InfoSource info2 = new InfoSource(null, ProviderType.Network, 10, "at.ac..adapterClass1", "at.ac...tranformclass1");
        info1.hasSetting("folder", "/tmp");

        settings.getSource().add(info1);
        settings.getSource().add(info2);
        System.out.println(settings.toJson());
    }

}
