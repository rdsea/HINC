/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.common.API;

import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import sinc.hinc.model.CloudServices.CloudProvider;
import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;
import sinc.hinc.model.VirtualNetworkResource.VNF;

/**
 * The HINC global API to query the information of different type of components. Most of the information query APIs have the followings parameters:
 * <p>
 * "timeout":, which is the delay to wait for the information. The reason to have timeout is HINCGlobal do not know how many HINC Local are available and send
 * back information. Set timeout=0 to let HINC Global to read from its local database, which contain the results of previous queries.
 * <p>
 * "hincUUID": the query should be send to this HINC Local. The hincUUID can be null or empty to broadcast.
 *
 * This level of API aims to hide the HINC Local from the user view, which provide a general access to the information grid.
 *
 * @author hungld
 */

@Path("/")
public interface HINCGlobalAPI {
    
    /**
     * To test the service is alive or not
     * @return Should be a date string
     */
    @GET
    @Path("/health")
    public String health();


    /**
     * Query the list of software defined gateways will all the information. Each software defined gateway is considerd as a single IoT provider in the view of
     * HINC.
     *
     * @param timeout The miliseconds to wait for the response.
     * @param hincUUID The HINC to query from
     * @return
     */
    @GET
    @Path("/SoftwareDefinedGateway")
    @Produces("application/json")
    public List<SoftwareDefinedGateway> querySoftwareDefinedGateways(
            @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @DefaultValue("") @QueryParam("hincUUID") String hincUUID
    );

    @GET
    @Path("/sdg/datapoint")
    @Produces("application/json")
    public List<DataPoint> queryDataPoint(
            @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

    @GET
    @Path("/sdg/controlpoint")
    @Produces("application/json")
    public List<ControlPoint> queryControlPoint(
            @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

    @GET
    @Path("/sdg/connectivity")
    @Produces("application/json")
    public List<CloudConnectivity> queryConnectivity(
            @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

    /**
     * This query the complete network service (which is not supported yet)
     *
     * @param timeout
     * @param hincUUID
     * @return
     */
    @GET
    @Path("/networkservice")
    @Produces("application/json")
    public List<NetworkFunctionService> queryNetworkService(
            @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

    /**
     * For individual virtual network functions. E.g. a single router component.
     *
     * @param timeout
     * @param hincUUID
     * @return
     */
    @GET
    @Path("/networkservice/VNF")
    @Produces("application/json")
    public List<VNF> queryVNF(
            @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

    @GET
    @Path("/cloudservice")
    @Produces("application/json")
    public List<CloudService> queryCloudServices(
            @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

    @GET
    @Path("/cloudprovider")
    @Produces("application/json")
    public List<CloudProvider> queryCloudProviders(
            @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

    /**
     * The single API to send control to the provider adaptor.
     * The context enables to capture generic QueryParameters by "context.getQueryParameters()" Note
     * that this control API is very generic to map with the provider adaptor
     *
     * @param context
     * @param controlAction
     */
    @POST
    @Path("/control/{providerUUID}/{action}")
    public void sendControl(@Context UriInfo context, @PathParam("providerUUID") String providerUUID, @PathParam("action") String controlAction);

}
