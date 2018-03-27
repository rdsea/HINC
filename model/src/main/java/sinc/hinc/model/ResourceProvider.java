package sinc.hinc.model;

import sinc.hinc.model.capabilities.ControlPoint;

import java.util.Collection;

public class ResourceProvider {

    private String name;
    private Collection<Resource> resources;
    private Collection<ControlPoint> managementPoints;

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
}
