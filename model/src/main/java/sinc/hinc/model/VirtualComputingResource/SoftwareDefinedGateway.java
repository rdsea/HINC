package sinc.hinc.model.VirtualComputingResource;

import sinc.hinc.model.PhysicalResource.PhysicalResource;
import sinc.hinc.model.VirtualComputingResource.Capability.Capability;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;

public class SoftwareDefinedGateway {

    /**
     * The uuid is unique within the whole system
     */
    private String uuid;

    /**
     * The name for human reading purpose
     */
    private String name;

    /**
     * The List of datapoint, controlpoint, connectivity and execution
     */
    private List<Capability> capabilities;

    /**
     * For custom data, e.g. created date, position, comments
     */
    private Map<String, String> meta;

    private List<AccessPoint> accessPoints;

    /**
     * Construction and get/set
     */
    public SoftwareDefinedGateway() {
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

    public List<Capability> getCapabilities() {
        if (capabilities == null) {
            capabilities = new ArrayList<>();
        }
        return capabilities;
    }

    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = capabilities;
    }

    public SoftwareDefinedGateway hasCapability(Capability capa) {
        capa.setGatewayID(this.getUuid());
        if (capabilities == null) {
            capabilities = new ArrayList<>();
        }
        this.capabilities.add(capa);
        return this;
    }

    public SoftwareDefinedGateway hasCapabilities(List<? extends Capability> capas) {
        if (capabilities == null) {
            capabilities = new ArrayList<>();
        }
        if (capas != null) {
            for (Capability capa : capas) {
                capa.setGatewayID(this.getUuid());
                this.capabilities.add(capa);
            }
        }
        return this;
    }

    public Map<String, String> getMeta() {
        if (meta == null) {
            meta = new HashMap<>();
        }
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public List<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(List<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static SoftwareDefinedGateway fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, SoftwareDefinedGateway.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @author hungld
     */
//    public class CapabilityEffect {
//
//        /**
//         * The id of the physical resource is affect
//         */
//        private PhysicalResource affectedEntity;
//
//        /**
//         * This show a list of effect that change the resource attribute, e.g. [sensorRate,+1] or [connectProtocol,mqtt]
//         */
//        private Map<String, String> effects = new HashMap<>();
//
//        public CapabilityEffect() {
//        }
//
//        public CapabilityEffect(PhysicalResource entity, String attribute, String effect) {
//            this.affectedEntity = entity;
//            effects.put(attribute, effect);
//        }
//
//        public PhysicalResource getAffectedEntity() {
//            return affectedEntity;
//        }
//
//        public void setAffectedEntity(PhysicalResource affectedEntity) {
//            this.affectedEntity = affectedEntity;
//        }
//
//        public Map<String, String> getEffects() {
//            return effects;
//        }
//
//        public void setEffects(Map<String, String> effects) {
//            this.effects = effects;
//        }
//
//    }
}
