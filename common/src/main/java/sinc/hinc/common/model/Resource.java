package sinc.hinc.common.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.annotation.Id;
import sinc.hinc.common.model.capabilities.ControlPoint;
import sinc.hinc.common.model.capabilities.DataPoint;

import java.util.ArrayList;
import java.util.Collection;

public class Resource {

    // add more as needed
    public enum ResourceType {
        IOT_RESOURCE,
        NETWORK_FUNCTION_SERVICE,
        CLOUD_SERVICE,
    }

    private String name;
    private String pluginName;
    private ResourceType resourceType;
    // can be gps or other representation
    // not formalized yet
    private String location;
    private JsonNode metadata;

    private Collection<ControlPoint> controlPoints = new ArrayList<>();
    private Collection<DataPoint> dataPoints = new ArrayList<>();
    // primary key used in database
    @Id
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JsonNode getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonNode metadata) {
        this.metadata = metadata;
    }

    public Collection<ControlPoint> getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(Collection<ControlPoint> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public Collection<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(Collection<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
