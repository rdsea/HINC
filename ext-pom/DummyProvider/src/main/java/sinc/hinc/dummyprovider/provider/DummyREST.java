/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.dummyprovider.controller.ChangePolicy;

/**
 *
 * @author hungld
 */
@Path("/")
@Produces("application/json")
public class DummyREST {

    static Logger logger = LoggerFactory.getLogger("Dummy");

    DummyData data;
    ChangePolicy changePolicy;
    boolean updatedPolicy = true;

    public DummyREST() {
        data = new DummyData(1000);
    }

    public DummyREST(int numberOfSensor) {
        data = new DummyData(numberOfSensor);
    }

    // create a Thread to change data
    @PostConstruct
    public void init() {
        updatePolicyAndStartControl();
    }

    ScheduledExecutorService executor = null;

    private void updatePolicyAndStartControl() {
        logger.debug("Updating change policy");
        if (this.executor != null && !this.executor.isShutdown()) {
            logger.debug("Shutdown the current executor");
            this.executor.shutdown();
        }
        this.executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = null;
        switch (this.changePolicy.getChangeType()) {
            case METADATA_CHANGE: {
                task = new DummyTaskMetaDataChange(changePolicy, data);
                break;
            }
            case SENSOR_IN_OUT:
                task = new DummyTaskSensorInOut(changePolicy, data);
                break;
        }
        logger.debug("Start to schedule change: " + this.changePolicy.getFrequency());
        executor.scheduleWithFixedDelay(task, 1, this.changePolicy.getFrequency(), TimeUnit.SECONDS);
    }

    @POST
    @Path("/changepolicy")
    public String updateChangePolicy(String data) {
        logger.debug("Updating change policy: " + data);
        ChangePolicy cp = ChangePolicy.fromJson(data);
        if (cp != null) {
            this.changePolicy = cp;
            updatePolicyAndStartControl();
            logger.debug("Update policy DONE !");
            return "true";
        }
        logger.debug("Update policy FAILED !");
        return "false";
    }

    @GET
    @Path("/health")
    public String health() {
        logger.debug("check health");
        return "ok";
    }

    @GET
    @Path("/items")
    public String getItems() {
        logger.debug("Querying datapoints...");
        logger.debug(data.getDataItems().get(0).toString());
        return data.toJson();
    }

    @GET
    @Path("/items/{id}")
    @Produces("application/json")
    public String getItemById(@PathParam("id") String id) {
        for (DummyMetadataItem item : data.getDataItems()) {
            if (item.getId().equals(id)) {
                return item.toJson();
            }
        }
        return null;
    }

}
