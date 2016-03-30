package at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability;

import at.ac.tuwien.dsg.hinc.model.PhysicalResource.PhysicalResource;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;

/**
 * The class represents for control point management
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DataPoint.class, name = "DataPoint"),
    @JsonSubTypes.Type(value = ControlPoint.class, name = "ControlPoint"),
    @JsonSubTypes.Type(value = CloudConnectivity.class, name = "CloudConectivity"),
    @JsonSubTypes.Type(value = ExecutionEnvironment.class, name = "ExecutionEnvironment")
})
public class Capability {

    /**
     * The name of the capability, e.g. start/stop/reconfigureXYZ
     */
    protected String name;

    /**
     * The ID of the resource, e.g. ID of sensor/actuator which was converted to the capability
     */
    protected String resourceID;

    /**
     * The ID of the gateway that the capability belongs
     */
    protected String gatewayID;

    protected String uuid;

    /**
     * The type of the capability
     */
    protected CapabilityType capabilityType;

    /**
     * Description
     */
    protected String description;

    /**
     * The physical resource give info. of what Things this SDG manages
     */
    private List<PhysicalResource> physicalResources;

    /**
     * Constructor, get/set
     */
    public Capability() {
    }

    public Capability(String resourceID, String name, CapabilityType type, String description) {
        this.resourceID = resourceID;
        this.name = name;
        this.capabilityType = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CapabilityType getCapabilityType() {
        return capabilityType;
    }

    public void setCapabilityType(CapabilityType capabilityType) {
        this.capabilityType = capabilityType;
    }

    public String getDescription() {
        return description;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGatewayID() {
        return gatewayID;
    }

    public void setGatewayID(String gatewayID) {
        this.gatewayID = gatewayID;
        this.uuid = gatewayID + "/" + resourceID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<PhysicalResource> getPhysicalResources() {
        return physicalResources;
    }

    public void setPhysicalResources(List<PhysicalResource> physicalResources) {
        this.physicalResources = physicalResources;
    }

}
