package sinc.hinc.model.VirtualComputingResource;

import sinc.hinc.model.Extensible.ExtensibleModel;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ExecutionEnvironment;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.ArrayList;
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
     * The name of the capability. E.g. data1/data2,control_start/stop
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

    /**
     * The uuid = gatewayID/name
     */
    protected String uuid;

    /**
     * Description
     */
    protected String description;

    /**
     * The physical resource give info. of what Things this SDG manages
     */
    private List<ExtensibleModel> extra;

    /**
     * Constructor, get/set
     */
    public Capability() {
    }

    public Capability(String resourceID, String name, String description) {
        this.resourceID = resourceID;
        this.name = name;
        this.description = description;
        this.gatewayID = "unknown";
        this.uuid = gatewayID + "/" + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<ExtensibleModel> getExtra() {
        if (this.extra == null){
            this.extra = new ArrayList<>();
        }
        return extra;
    }

    public void setExtra(List<ExtensibleModel> extra) {
        this.extra = extra;
    }
    
    public ExtensibleModel getExtraByType(Class clazz){
        for(ExtensibleModel ext: this.getExtra()){
            if (ext.getClazz().equals(clazz)){
                return ext;
            }
        }
        return null;
    }

}
