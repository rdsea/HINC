package at.ac.tuwien.dsg.hinc.model.CloudServices;

import at.ac.tuwien.dsg.hinc.model.VirtualNetworkResource.AccessPoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class CloudService {
    private AccessPoint connectVia;    
    private DataCenter dataCenter;
    
    private String type;
    
    private Map<String, String> attributes;

    public CloudService() {
    }

    /**
     * This construction is used for creating template, which take type as parameter
     * @param type 
     */
    public CloudService(String type) {
        this.type = type;
    }
    

    public AccessPoint getConnectVia() {
        return connectVia;
    }

    public void setConnectVia(AccessPoint connectVia) {
        this.connectVia = connectVia;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
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
    
    
    
    public void hasAttribute(String key, String val){
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(key, val);
    }
    
    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }
    
}
