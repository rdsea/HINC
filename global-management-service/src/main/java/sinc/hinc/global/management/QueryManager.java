/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.management;

import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualNetworkResource.VNF;

import java.util.List;

/**
 * This class access to the database and return information
 *
 * @author hungld
 */
public class QueryManager {

    /**
     * This method query data points.
     *
     * @param dpTemplate This contain basic information, which will be mapped with actual data. This object work like a predefined requirements.
     * @return a list of data points
     */
    public static List<DataPoint> QueryDataPoints(DataPoint dpTemplate) {
        // TODO: Implement this to query data points
        return null;
    }

    public static List<VNF> QueryVNF(VNF vnfTemplate) {
        // TODO: Implement this to query data points
        return null;
    }
}
