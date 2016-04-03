package at.ac.tuwien.dsg.hinc.model.PhysicalResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    protected List<ExtensibleModel> extensions;

    public PhysicalResource() {
    }

    public PhysicalResource(ExtensibleModel... exts) {
        this.extensions = new ArrayList<>();
        this.extensions.addAll(Arrays.asList(exts));
    }

    public ResourceCategory getCategory() {
        return category;
    }

    public void setCategory(ResourceCategory category) {
        this.category = category;
    }

    public List<ExtensibleModel> getExtensions() {
        if (extensions == null) {
            extensions = new ArrayList<>();
        }
        return extensions;
    }

    public void setExtensions(List<ExtensibleModel> extensions) {
        this.extensions = extensions;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

}
