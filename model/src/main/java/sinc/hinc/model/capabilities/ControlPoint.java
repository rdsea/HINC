package sinc.hinc.model.capabilities;

import com.fasterxml.jackson.databind.JsonNode;
import sinc.hinc.model.accessPoint.AccessPoint;

import java.util.ArrayList;
import java.util.Collection;

public class ControlPoint {

    // add more as needed
    public enum ControlType {
        PROVISION,
        CONFIGURE,
        EXECUTE,
        REMOVE,
    }

    private String name;
    private JsonNode parameters;
    private ControlType controlType;
    private Collection<AccessPoint> accessPoints = new ArrayList<>();

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

    public ControlType getControlType() {
        return controlType;
    }

    public void setControlType(ControlType controlType) {
        this.controlType = controlType;
    }

    public Collection<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(Collection<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }
}
