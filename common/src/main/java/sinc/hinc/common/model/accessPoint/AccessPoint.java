package sinc.hinc.common.model.accessPoint;

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
