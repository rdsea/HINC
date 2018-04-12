/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.common.API;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.common.metadata.HincLocalMeta;

/**
 * The API to manage HINC components including the HINC Global Services and HINC Local Services.
 * In the new version, the Local/Global Management Service becomes part of rsiHub
 * @author hungld
 */
@Path("/")
@Api(value = "rsiHub Management API")
@SwaggerDefinition(
        info = @Info(
                title = "rsiHub Management API",
                version = "1.1",
                description = "This API provides functions to manage the rsiHub services",
                license = @License(name = "APACHE LICENSE", url = "http://www.apache.org/licenses/LICENSE-2.0")),
        tags = @Tag(name = "Public", description = "This API is for public usage"),
        schemes = {SwaggerDefinition.Scheme.HTTP},
        consumes = {"application/json"},
        produces = {"application/json"}
)
public interface HINCManagementAPI {

    /**
     * 
     * 
     * @return
     */
    
    @GET
    @Path("/meta")
    @Produces("application/json")
    @ApiOperation(value = "Get rsiHub Global metadata",
            notes = "rsiHub Global Meta include the communication group, communication middleware and location of the service.",
            response = HINCGlobalMeta.class)
    public HINCGlobalMeta getHINCGlobalMeta();

    /**
     * Set the HINCGlobal settings. The next query of HINC Global will follow this
     *
     * @param meta The meta is send via post data
     */
    @POST
    @Path("/meta")
    @Consumes("application/json")
    @ApiOperation(value = "Update this rsiHub Global metadata",
            notes = "This merge the rsiHub global meta, e.g. to update the communication group and middleware. The fields which are null or empty will not be updated.",
            response = void.class)
    public void setHINCGlobalMeta(
             @ApiParam(value = "The metadata of rsiHub Global in JSON", required = true) HINCGlobalMeta meta);

    /**
     * Collect all the HINC locals information.
     *
     * @param timeout The miliseconds to wait for the response. If timeout is 0, HINC Global will read on its database only. The default is 2 seconds.
     *
     * @return A list of HINC Locals.
     */
    @GET
    @Path("/rsiHub")
    @Produces("application/json")
    @Consumes("application/json")
    @ApiOperation(value = "Query the list of rsiHub Local Management Service",
            notes = "The list of rsiHub Local Management Service which are joining the same communication group",
            response = HincLocalMeta.class,
            responseContainer = "List")
    public List<HincLocalMeta> queryHINCLocal(
            @ApiParam(value = "The miliseconds to wait for the response", required = false, defaultValue = "2000") @DefaultValue("2000") @QueryParam("timeout") int timeout);
    
    @POST
    @Path("/rsiHub/{localUUID}/infobase")
    @Consumes("text/plain")
     @ApiOperation(value = "Update the information base for a local service, which support Information Centric Network query.",
            notes = "This function should be in the local service itself. This API is a quick way to manually set the Information Base",
            response = HincLocalMeta.class,
            responseContainer = "String")
    public void setHINCLocalTopic(
            @ApiParam(value = "List of namespace, separated by comma", required = true) String infoBase,
            @ApiParam(value = "The UUID of the rsiHub Local Management Service", required = true) @PathParam("localUUID") String localUUID,
            @ApiParam(value = "True: append more infobase, False: clear and update new infobase", required = false, defaultValue = "false") @DefaultValue("false") @QueryParam("append") String append
    );
}
