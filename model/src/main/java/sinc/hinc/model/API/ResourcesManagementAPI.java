/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.API;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
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
 *
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
@Api(value = "Resource Management API")
@SwaggerDefinition(
        info = @Info(
                title = "Resource management API",
                version = "1.0",
                description = "This API provides functions to query the information of distributed resources.",
                license = @License(name = "APACHE LICENSE", url = "http://www.apache.org/licenses/LICENSE-2.0")),
        tags = @Tag(name = "Public", description = "This API is for public usage"),
        schemes = (SwaggerDefinition.Scheme.HTTP),
        consumes = {"application/json"},
        produces = {"application/json"}
)
public interface ResourcesManagementAPI {

    final String timeoutParameterDescription = "The miliseconds to wait for the response. Set to 0 to query from HINC Global database.";
    final String hincUUIDParameterDescription = "The HINC to query from. Leave empty to query from all HINC Local.";
    
    @GET
    @Path("/SoftwareDefinedGateway")
    @Produces("application/json")
    @ApiOperation(value = "Query all capabilities on the IoT site",
            notes = "All the capabilities will be wrapped in a SoftwareDefinedGateway object. The information contains also the metadata of the site.",
            response = SoftwareDefinedGateway.class,
            responseContainer = "List")
    public List<SoftwareDefinedGateway> querySoftwareDefinedGateways(
            @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = hincUUIDParameterDescription, required = false, defaultValue = "null") @DefaultValue("") @QueryParam("hincUUID") String hincUUID
    );

    @GET
    @Path("/sdg/datapoint")
    @Produces("application/json")
    @ApiOperation(value = "Query the list of the data points",
            notes = "The list of the data points available from a single or multiple sites",
            response = DataPoint.class,
            responseContainer = "List")
    public List<DataPoint> queryDataPoint(
            @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = hincUUIDParameterDescription, required = false, defaultValue = "null") @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

                    
    @GET
    @Path("/sdg/controlpoint")
    @Produces("application/json")
    @ApiOperation(value = "Query the list of the control points",
            notes = "The list of the control points available from a single or multiple sites",
            response = ControlPoint.class,
            responseContainer = "List")
    public List<ControlPoint> queryControlPoint(
            @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = hincUUIDParameterDescription, required = false, defaultValue = "null") @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

    @GET
    @Path("/sdg/connectivity")
    @Produces("application/json")
    @ApiOperation(value = "Query the list of the cloud connectivities",
            notes = "The list of the cloud connectivities  available from a single or multiple sites",
            response = CloudConnectivity.class,
            responseContainer = "List")
    public List<CloudConnectivity> queryConnectivity(
            @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = hincUUIDParameterDescription, required = false, defaultValue = "null") @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

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
    @ApiOperation(value = "Query the list of the network services",
            notes = "The list of the Network Function Services available from a single or multiple sites",
            response = NetworkFunctionService.class,
            responseContainer = "List")
    public List<NetworkFunctionService> queryNetworkService(
            @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = hincUUIDParameterDescription, required = false, defaultValue = "null") @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

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
    @ApiOperation(value = "Query the list of the virtual network functions",
            notes = "The list of the Virtual Network Functions available from a single or multiple sites",
            response = VNF.class,
            responseContainer = "List")
    public List<VNF> queryVNF(
            @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = hincUUIDParameterDescription, required = false, defaultValue = "null") @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

    @GET
    @Path("/cloudservice")
    @Produces("application/json")
    @ApiOperation(value = "Query the list of the cloud services",
            notes = "The list of the cloud services available from a single or multiple sites",
            response = CloudService.class,
            responseContainer = "List")
    public List<CloudService> queryCloudServices(
           @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = hincUUIDParameterDescription, required = false, defaultValue = "null") @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

    @GET
    @Path("/cloudprovider")
    @Produces("application/json")
    @ApiOperation(value = "Query the list of the cloud providers",
            notes = "The list of the cloud providers available from a single or multiple sites",
            response = CloudProvider.class,
            responseContainer = "List")
    public List<CloudProvider> queryCloudProviders(
            @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = hincUUIDParameterDescription, required = false, defaultValue = "null") @DefaultValue("") @QueryParam("hincUUID") String hincUUID);


    @POST
    @Path("/control/{providerUUID}/{action}")
    @ApiOperation(value = "Send a control action to a provider",
            notes = "The single API to send control to the provider adaptor. "
                    + "The context enables to capture generic QueryParameters by \"context.getQueryParameters()\" "
                    + "Note that this control API is very generic to map with the provider adaptor",
            response = CloudProvider.class,
            responseContainer = "List")
    public void sendControl(@Context UriInfo context, 
            @ApiParam(value = "The UUID of the provider to send the control command to", required = true) @PathParam("providerUUID") String providerUUID,
            @ApiParam(value = "The name of the action to be send", required = true)@PathParam("action") String controlAction);

}
