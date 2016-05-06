/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.common.API;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.common.metadata.HincLocalMeta;

/**
 *
 * @author hungld
 */
@Path("/")
public interface HINCManagementAPI {
    /**
     * Get this HINC Global metadata. E.g. the default group, communication middleware
     *
     * @return
     */
    @GET
    @Path("/meta")
    @Produces("application/json")
    public HINCGlobalMeta getHINCGlobalMeta();

    /**
     * Set the HINCGlobal settings. The next query of HINC Global will follow this
     *
     * @param meta The meta is send via post data
     */
    @POST
    @Path("/meta")
    @Consumes("application/json")
    public void setHINCGlobalMeta(HINCGlobalMeta meta);

    /**
     * Collect all the HINC locals information.
     *
     * @param timeout The miliseconds to wait for the response. If timeout is 0, HINC Global will read on its database only. The default is 2 seconds.
     *
     * @return A list of HINC Locals.
     */
    @GET
    @Path("/hinc")
    @Produces("application/json")
    public List<HincLocalMeta> queryHINCLocal(@DefaultValue("2000") @QueryParam("timeout") int timeout);
}
