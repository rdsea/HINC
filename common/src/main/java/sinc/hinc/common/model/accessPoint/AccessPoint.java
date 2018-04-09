package sinc.hinc.common.model.accessPoint;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


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
        HTTP,
        MQTT,
    }

    private AccessPointType accessPointType;
    private String uri;

    public AccessPointType getAccessPointType() {
        return accessPointType;
    }

    public void setAccessPointType(AccessPointType accessPointType) {
        this.accessPointType = accessPointType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
