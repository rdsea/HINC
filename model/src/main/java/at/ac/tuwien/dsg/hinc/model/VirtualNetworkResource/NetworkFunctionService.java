package at.ac.tuwien.dsg.hinc.model.VirtualNetworkResource;

import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.extensions.NetworkQuality;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkFunctionService {

    private List<VNFForwardGraph> vnfForwardGraphs;
    private List<AccessPoint> accessPoints;
    private NetworkQuality quality;

    public NetworkFunctionService() {
    }

    public List<VNFForwardGraph> getVnfForwardGraphs() {
        if (vnfForwardGraphs == null) {
            vnfForwardGraphs = new ArrayList<>();
        }
        return vnfForwardGraphs;
    }

    public void setVnfForwardGraphs(List<VNFForwardGraph> vnfForwardGraphs) {
        this.vnfForwardGraphs = vnfForwardGraphs;
    }

    public List<AccessPoint> getAccessPoints() {
        if (accessPoints == null) {
            this.accessPoints = new ArrayList<>();
        }
        return accessPoints;
    }

    public void setAccessPoints(List<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public NetworkQuality getQuality() {
        if (quality == null){
            quality = new NetworkQuality();
        }
        return quality;
    }

    public void setQuality(NetworkQuality quality) {
        this.quality = quality;
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
