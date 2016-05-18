/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider;

import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;

/**
 *
 * @author hungld
 */
@Path("/")
public class DummyREST {

    @GET
    @Path("/datapoints")
    @Produces("application/json")
    public List<DataPoint> getItems() {
        return DummyData.dataPoints;
    }

    @GET
    @Path("/datapoints/{uuid}")
    @Produces("application/json")
    public DataPoint getItem(String uuid) {
        for (DataPoint dp : DummyData.dataPoints) {
            if (dp.getUuid().endsWith(uuid)) {
                return dp;
            }
        }
        return null;
    }

    // to return a MAP of <uuid,status>. The status can be: update, remove, appear
    @GET
    @Path("/datapoints/subscribe/stream")
    @Produces("application/json")
    public Map<String,String> subscribeDataPointAMQP() {
        // TODO: to implement this
        return null;
    }
    
    // return a ampq endpoint
    @GET
    @Path("/datapoints/subscribe/amqp")
    @Produces("application/json")
    public String subscribeDataPointAMQP(@QueryParam("url") String amqpURL) {
        // TODO: to implement this
        return null;
    }

}
