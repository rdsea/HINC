package sinc.hinc.common.model;

import com.fasterxml.jackson.databind.JsonNode;
import sinc.hinc.common.model.capabilities.ControlPoint;

import java.util.Collection;

public class ResourceProvider {

    private String name;
    private Collection<Resource> resources;
    private Collection<ControlPoint> managementPoints;
    // set of resources available from the provider
    private Collection<Resource> catalogue;
    private String uuid;


    public Collection<Resource> getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(Collection<Resource> catalogue) {
        this.catalogue = catalogue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Resource> getResources() {
        return resources;
    }

    public void setResources(Collection<Resource> resources) {
        this.resources = resources;
    }

    public Collection<ControlPoint> getManagementPoints() {
        return managementPoints;
    }

    public void setManagementPoints(Collection<ControlPoint> managementPoints) {
        this.managementPoints = managementPoints;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
