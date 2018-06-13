package sinc.hinc.common.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.annotation.Id;
import sinc.hinc.common.model.capabilities.ControlPoint;

import java.util.Collection;

public class ResourceProvider {

    private String name;
    private Collection<Resource> availableResources; // aka catalogue
    // set of resources available from the provider
    @Id
    private String uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Resource> getAvailableResources() {
        return availableResources;
    }

    public void setAvailableResources(Collection<Resource> resources) {
        this.availableResources = resources;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
