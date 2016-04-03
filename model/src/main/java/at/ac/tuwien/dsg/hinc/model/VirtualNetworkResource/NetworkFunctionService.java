package at.ac.tuwien.dsg.hinc.model.VirtualNetworkResource;

import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.extensions.NetworkQuality;
import java.util.ArrayList;
import java.util.List;

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
        return quality;
    }

    public void setQuality(NetworkQuality quality) {
        this.quality = quality;
    }
    
    

}
