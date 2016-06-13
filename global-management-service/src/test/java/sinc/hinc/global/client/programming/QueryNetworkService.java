/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.client.programming;

import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;

/**
 * @author hungld
 */
public class QueryNetworkService {

    public static void main(String[] args) throws Exception {
        NetworkFunctionService nfsTemplate = new NetworkFunctionService();
        nfsTemplate.getQuality().setBandwidth("16 GB/s");
        CloudService cloudTemplate = new CloudService("storage");
        cloudTemplate.hasAttribute("capacity", "1 TB");

    }
}
