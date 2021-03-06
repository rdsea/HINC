package at.ac.tuwien.dsg.hinc.globalmanagementservice.model;

import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.ResourceProvider;

import java.util.List;

public class LocalMS {
    private String id;
    private String group;
    private HincLocalMeta hincLocalMeta;
    private List<Resource> resources;
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

    public HincLocalMeta getHincLocalMeta() {
        return hincLocalMeta;
    }

    public void setHincLocalMeta(HincLocalMeta hincLocalMeta) {
        this.hincLocalMeta = hincLocalMeta;
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

