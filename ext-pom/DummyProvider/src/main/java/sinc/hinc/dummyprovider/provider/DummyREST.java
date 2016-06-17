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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author hungld
 */
@Path("/")
@Produces("application/json")
public class DummyREST {

    DummyData data;

    public DummyREST() {
        data = new DummyData(1000);
    }
    
    public DummyREST(int numberOfSensor) {
        data = new DummyData(numberOfSensor);
    }
    

    @GET
    @Path("/health")
    public String health() {
        System.out.println("check health");
        return "ok";
    }

    @GET
    @Path("/datapoints")
    public String getDataPoints() {
        System.out.println("Querying datapoints...");
        System.out.println(data.getDataItems().get(0).toString());
        return data.toJson();
    }

    @GET
    @Path("/datapoints/quantity/{quantity}")
    @Produces("application/json")
    public String generateDataPoint(@PathParam("quantity") int quantity) {
        DummyData dataPart = new DummyData(0);
        dataPart.getDataItems().addAll(data.getDataItems().subList(0, quantity));
        return dataPart.toJson();
    }

    @GET
    @Path("/datapoints/{id}")
    @Produces("application/json")
    public DummyMetadataItem getItem(@PathParam("id") String id) {
        for (DummyMetadataItem item : data.getDataItems()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    // to return a MAP of <uuid,status>. The status can be: update, remove, appear
    @GET
    @Path("/datapoints/subscribe/stream")
    @Produces("application/json")
    public Map<String, String> subscribeDataPointAMQP() {
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
