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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import sinc.hinc.model.CloudServices.CloudProvider;
import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.VirtualResource;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.NetworkService;
import sinc.hinc.model.VirtualNetworkResource.VNF;
import sinc.hinc.model.slice.AppSlice;
import sinc.hinc.model.slice.InfrastructureSlice;

/**
 * The HINC global API to query the information of different type of components.
 * Most of the information query APIs have the followings parameters:
 *
 * <p>
 * "timeout":, which is the delay to wait for the information. The reason to
 * have timeout is HINCGlobal do not know how many HINC Local are available and
 * send back information. Set timeout=0 to let HINC Global to read from its
 * local database, which contain the results of previous queries.
 * <p>
 * "hincUUID": the query should be send to this HINC Local. The hincUUID can be
 * null or empty to broadcast.
 *
 * This level of API aims to hide the HINC Local from the user view, which
 * provide a general access to the information grid.
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
    @Path("/IoTUnits")
    @Produces("application/json")
    @ApiOperation(value = "Query all capabilities on the IoT site",
            notes = "All the capabilities will be wrapped in a IoTUnit object. The information contains also the metadata of the site.",
            response = SoftwareDefinedGateway.class,
            responseContainer = "List")
    Set<VirtualResource> queryIoTUnits(
            @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = hincUUIDParameterDescription, required = false, defaultValue = "null") @DefaultValue("") @QueryParam("hincUUID") String hincUUID,
            @ApiParam(value = "The namespaces that IoT Unit belong to. Separate namespaces in the list with commas", required = false, defaultValue = "null") @DefaultValue("") @QueryParam("infoBases") String infoBases,
            @ApiParam(value = "The maximum records to return from each HINC Local.", required = false, defaultValue = "0") @DefaultValue("0") @QueryParam("limit") int limit,
            @ApiParam(value = "To force the HINC Local to rescan resource", required = false, defaultValue = "false") @DefaultValue("false") @QueryParam("rescan") String forceRescan
    );

    @GET
    @Path("/IoTProviders")
    @Produces("application/json")
    @ApiOperation(value = "Query all IoT providers",
            notes = "The APIs of the IoT providers on the gateway.",
            response = SoftwareDefinedGateway.class,
            responseContainer = "List")
    Set<ResourcesProvider> queryResourceProviders(
            @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = hincUUIDParameterDescription, required = false, defaultValue = "null") @DefaultValue("") @QueryParam("hincUUID") String hincUUID,
            @ApiParam(value = "The namespaces that providers belong to. Separate namespaces in the list with commas", required = false, defaultValue = "null") @DefaultValue("") @QueryParam("infoBases") String infoBases,
            @ApiParam(value = "The maximum records to return from each HINC Local.", required = false, defaultValue = "0") @DefaultValue("0") @QueryParam("limit") int limit,
            @ApiParam(value = "To force the HINC Local to rescan resource", required = false, defaultValue = "false") @DefaultValue("false") @QueryParam("rescan") String forceRescan
    );

    @GET
    @Path("/sdg/datapoint")
    @Produces("application/json")
    @ApiOperation(value = "Query the list of the data points",
            notes = "The list of the data points available from a single or multiple sites",
            response = DataPoint.class,
            responseContainer = "List")
    public Collection<DataPoint> queryDataPoint(
            @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = "The namespaces that IoT Unit belong to. Separate namespaces in the list with commas", required = false, defaultValue = "null") @DefaultValue("") @QueryParam("infoBases") String infoBases,
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
            @ApiParam(value = "The namespaces that IoT Unit belong to. Separate namespaces in the list with commas", required = false, defaultValue = "null") @DefaultValue("") @QueryParam("infoBases") String infoBases,
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
            response = NetworkService.class,
            responseContainer = "List")
    public Collection<NetworkService> queryNetworkService(
            @ApiParam(value = timeoutParameterDescription, required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout,
            @ApiParam(value = hincUUIDParameterDescription, required = false, defaultValue = "null") @DefaultValue("") @QueryParam("hincUUID") String hincUUID);

    @POST
    @Path("/networkservice")
    @Produces("application/json")
    @Consumes("applicaiton/json")
    @ApiOperation(value = "Register a new network service",
            notes = "A json defines the network service is sent. Return the JSON object in database after saving",
            response = String.class,
            responseContainer = "String")
    public String addNetworkService(NetworkService networkService);

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
    @Path("/control/{gatewayid}/{resourceid}/{actionName}")
    @Produces("text/plain")
    @ApiOperation(value = "Send a control action to a provider. This API returns a message from low level runtime.",
            notes = "The single API to send control to the provider adaptor. "
            + "The parameter is a String. Multiple parameter is separate by space.\" "
            + "The whole parameter (gatewayid, resourceid, actionName) can be get from the control point UUID",
            response = String.class,
            responseContainer = "String")
    public String sendControl(
            @ApiParam(value = "The UUID of the gateway to send the control command to", required = true) @PathParam("gatewayid") String gatewayid,
            @ApiParam(value = "The resource ID on the gateway to control", required = true) @PathParam("resourceid") String resourceid,
            @ApiParam(value = "The action name ", required = true) @PathParam("actionName") String actionName,
            @ApiParam(value = "The parameter for the control", required = false) @QueryParam("param") String param);
    
    @POST
    @Path("/slides/data")
    public AppSlice configureDataSlide(String datapointID, String networkID, String cloudServiceID);

    @POST
    @Path("/slides/infrastructure")
    public InfrastructureSlice configureInfrastructureSlide(String gatewayID, String networkID, String cloudServiceID);

}
