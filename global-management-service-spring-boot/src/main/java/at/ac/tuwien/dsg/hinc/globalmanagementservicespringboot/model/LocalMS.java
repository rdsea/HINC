package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.ResourceProvider;

import java.util.List;

public class LocalMS {
    @Id
    private String id;
    private String group;
    @DBRef
    private List<Resource> resources;
    @DBRef
    private List<ResourceProvider> resourceProviders;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<ResourceProvider> getResourceProviders() {
        return resourceProviders;
    }

    public void setResourceProviders(List<ResourceProvider> resourceProviders) {
        this.resourceProviders = resourceProviders;
    }
}

