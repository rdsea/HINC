package at.ac.tuwien.dsg.hinc.model.PhysicalResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhysicalResource {

    /**
     * The resource can be sensor/actuator/gateway/field device
     *
     */
    private ResourceCategory category;

    /**
     * Supplement for the category, more concrete
     */
    private ResourceType type;

    /**
     * The domain model
     */
    protected Map<String, ExtensibleModel> domain;

    public PhysicalResource() {
    }

    public PhysicalResource(ExtensibleModel... exts) {
        this.domain = new HashMap<>();
        for (ExtensibleModel e : exts) {
            this.domain.put(e.getClass().getSimpleName(), e);
        }
    }

    public ResourceCategory getCategory() {
        return category;
    }

    public void setCategory(ResourceCategory category) {
        this.category = category;
    }

    public Map<String, ExtensibleModel> getDomain() {
        if (domain == null) {
            domain = new HashMap<>();
        }
        return domain;
    }

    public void setDomain(Map<String, ExtensibleModel> domain) {
        this.domain = domain;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

}
