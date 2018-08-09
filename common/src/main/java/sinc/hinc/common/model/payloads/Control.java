package sinc.hinc.common.model.payloads;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.annotation.Id;
import sinc.hinc.common.model.accessPoint.AccessPoint;
import sinc.hinc.common.model.capabilities.ControlPoint;

import java.util.ArrayList;
import java.util.Collection;

public class Control {

    private String controlPointUuid;
    private String resourceProviderUuid;
    @Id
    private String uuid;

    private String name;
    private JsonNode parameters;
    private ControlPoint.ControlType controlType;
    private Collection<AccessPoint> accessPoints = new ArrayList<>();
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getControlPointUuid() {
        return controlPointUuid;
    }

    public void setControlPointUuid(String controlPointUuid) {
        this.controlPointUuid = controlPointUuid;
    }

    public String getResourceProviderUuid() {
        return resourceProviderUuid;
    }

    public void setResourceProviderUuid(String resourceProviderUuid) {
        this.resourceProviderUuid = resourceProviderUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getParameters() {
        return parameters;
    }

    public void setParameters(JsonNode parameters) {
        this.parameters = parameters;
    }

    public ControlPoint.ControlType getControlType() {
        return controlType;
    }

    public void setControlType(ControlPoint.ControlType controlType) {
        this.controlType = controlType;
    }

    public Collection<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(Collection<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
