package sinc.hinc.model.VirtualNetworkResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;

public class NetworkService {

    public enum NetworkServiceType {
        BROKER_MQTT, BROKER_COAP,
        ROUTER_GENERIC, ROUTER_WEAVE
    }

    String uuid;
    String name;
    NetworkServiceType type;

    // a Network service can have a access point to let other service connect to
    AccessPoint accessPoint;

    public NetworkService() {
    }

    public NetworkService(String uuid, String name, NetworkServiceType type, AccessPoint accessPoint) {
        this.uuid = uuid;
        this.name = name;
        this.type = type;
        this.accessPoint = accessPoint;
    }

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

    public NetworkServiceType getType() {
        return type;
    }

    public void setType(NetworkServiceType type) {
        this.type = type;
    }

    public AccessPoint getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(AccessPoint accessPoint) {
        this.accessPoint = accessPoint;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.uuid);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NetworkService other = (NetworkService) obj;
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        return true;
    }

}
