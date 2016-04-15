/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.client.programming;

import at.ac.tuwien.dsg.hinc.model.CloudServices.CloudService;
import at.ac.tuwien.dsg.hinc.model.PhysicalResource.PhysicalResource;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.extensions.Location;
import at.ac.tuwien.dsg.hinc.model.VirtualNetworkResource.AccessPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualNetworkResource.NetworkFunctionService;
import java.util.Arrays;

/**
 *
 * @author hungld
 */
public class SliceExamples {
    public static void main(String[] args){
        SoftwareDefinedGateway gw = new SoftwareDefinedGateway();
        gw.setName("gateway1");
        DataPoint dp = new DataPoint("BodyTemperature");
        dp.setResourceID("00:3b:B6:BodyTemperature");
        dp.setMeasurementUnit("celcius");
        ControlPoint cp = new ControlPoint(dp.getResourceID(), "changeRate", "change sensor rate");
        
        gw.getCapabilities().add(dp);
        gw.getCapabilities().add(cp);
        
        Location location = new Location("48.172955", "16.325684", "25");
        dp.getExtra().add(new PhysicalResource(location));
        
        System.out.println(gw.toJson());
        
        System.out.println("================000===============");
        
        
        NetworkFunctionService nfsTemplate = new NetworkFunctionService();
        nfsTemplate.getQuality().setBandwidth("16 GB/s");
        AccessPoint ap = new AccessPoint("10.99.0.5");
        nfsTemplate.setAccessPoints(Arrays.asList(ap));
        
        
        CloudService cloudTemplate = new CloudService("storage");
        cloudTemplate.hasAttribute("capacity", "1 TB");
        
        System.out.println(nfsTemplate.toJson());
        
        System.out.println(cloudTemplate.toJson());
    }
}
