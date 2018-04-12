package sinc.hinc.common.model.accessPoint;

import java.util.Collection;

public class MqttAccessPoint extends AccessPoint {

    public int qos;
    public Collection<String> topics;

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public Collection<String> getTopics() {
        return topics;
    }

    public void setTopics(Collection<String> topics) {
        this.topics = topics;
    }
}
