package sinc.hinc.model.CloudServices;

import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

/**
 * A generic class to view the cloud services (which is the instances of the services). A service can be atomic, e.g. VMs, volumes or composite, e.g. the
 * DataBase cluster, Elastic EventProcessing Unit
 *
 * @author hungld
 */
public class CloudService {

    // a service should have an access point. The most basic is its IP address (local/public), or RESTful endpoint.
    // service can be access only via localhost.
    private AccessPoint accessPoint;

    // the provider who managed this. The IaaS can manage VM/docker, the higher tool like SALSA, Chef can manage application level services.
    private String providerUUID;

    // the service are hosted on particular VM/docker/container. If this can be capture, we know how to reconfigure the service.
    private String hostedOnUUID;

    // the generic type of the service. We do not standalize it yet.
    private String type;

    // the generic map for describing the service
    private Map<String, String> attributes;

    public CloudService() {
    }

    /**
     * This construction is used for creating template, which take type as parameter
     *
     * @param type
     */
    public CloudService(String type) {
        this.type = type;
    }

    public void hasAttribute(String key, String val) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(key, val);
    }

    public AccessPoint getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(AccessPoint accessPoint) {
        this.accessPoint = accessPoint;
    }

    public String getProviderUUID() {
        return providerUUID;
    }

    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }

    public String getHostedOnUUID() {
        return hostedOnUUID;
    }

    public void setHostedOnUUID(String hostedOnUUID) {
        this.hostedOnUUID = hostedOnUUID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

}
