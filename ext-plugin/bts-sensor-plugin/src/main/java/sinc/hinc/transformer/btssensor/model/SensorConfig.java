package sinc.hinc.transformer.btssensor.model;

import java.util.HashMap;
import java.util.Map;

public class SensorConfig {
    private String uri;
    private String topic;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Map<String, String> asMap(){
        Map<String, String> map = new HashMap<>();

        map.put("url", this.uri);
        map.put("topic", this.topic);
        return map;
    }
}
