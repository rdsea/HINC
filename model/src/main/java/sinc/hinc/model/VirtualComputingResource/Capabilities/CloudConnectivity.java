/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource.Capabilities;

import sinc.hinc.model.VirtualComputingResource.Capability;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.model.VirtualNetworkResource.NetworkService;

/**
 * It define the network which this component is following
 *
 * @author hungld
 */
public class CloudConnectivity extends Capability {

    NetworkService.NetworkServiceType networkType;

    AccessPoint connectingTo;

    // note the "name" of this capability is the network interface, e.g. eth0, etc
    // connection mode: 3G, 4G, WIFI, 
//    String mode;
    // the IP and port of the gateway/router that the resource link to
    // this somehow define the routing strategy of this connection
//    String defaultGateway;
    // the IPv4 of the interface
//    String IP;
    // the MAC to identify the device
//    String MAC;
    // store subnet mask, to determine subnetwork
//    String network;
    // some control to turn on/off the device
//    ControlPoint controlChangeProtocol = null;
    /**
     * **************
     * GETER/SETTER * **************
     */
    public CloudConnectivity() {
    }

    public CloudConnectivity(String resourceID, String name, String description) {
        super(resourceID, name, description);
    }

//    public CloudConnectivity(String resourceID, String name, String description, String IP, String MAC) {
//        super(resourceID, name, description);
//        this.IP = IP;
//        this.MAC = MAC;
//    }
//
//    public String getDefaultGateway() {
//        return defaultGateway;
//    }
//
//    public void setDefaultGateway(String defaultGateway) {
//        this.defaultGateway = defaultGateway;
//    }
//
//    public ControlPoint getControlChangeProtocol() {
//        return controlChangeProtocol;
//    }
//
//    public void setControlChangeProtocol(ControlPoint controlChangeProtocol) {
//        this.controlChangeProtocol = controlChangeProtocol;
//    }
//
//    public String getMode() {
//        return mode;
//    }
//
//    public void setMode(String mode) {
//        this.mode = mode;
//    }
//
//    public String getIP() {
//        return IP;
//    }
//
//    public void setIP(String IP) {
//        this.IP = IP;
//    }
//
//    public String getMAC() {
//        return MAC;
//    }
//
//    public void setMAC(String MAC) {
//        this.MAC = MAC;
//    }

}
