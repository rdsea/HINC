package sinc.hinc.common.model.accessPoint;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Id;


// annotations to deserialize child classes properly
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "accessPointType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HTTPAccessPoint.class, name = "HTTP"),
        @JsonSubTypes.Type(value = MqttAccessPoint.class, name = "MQTT")
})
public class AccessPoint {

    // add more as needed
    public enum AccessPointType{
        REST,
        MQTT,
    }

    public enum NetworkProtocol{
        IP,
        LoRaWAN,
        NB_IOT
    }

    public enum AccessPattern{
        PUBSUB,
        QUEUE,
        REQUEST_RESPONSE
    }

    private AccessPointType applicationProtocol;
    @Id
    private String uri;
    private NetworkProtocol networkProtocol;
    private AccessPattern accessPattern;
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public NetworkProtocol getNetworkProtocol() {
        return networkProtocol;
    }

    public void setNetworkProtocol(NetworkProtocol networkProtocol) {
        this.networkProtocol = networkProtocol;
    }

    public AccessPattern getAccessPattern() {
        return accessPattern;
    }

    public void setAccessPattern(AccessPattern accessPattern) {
        this.accessPattern = accessPattern;
    }

    public AccessPointType getApplicationProtocol() {
        return applicationProtocol;
    }

    public void setApplicationProtocol(AccessPointType accessPointType) {
        this.applicationProtocol = accessPointType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
