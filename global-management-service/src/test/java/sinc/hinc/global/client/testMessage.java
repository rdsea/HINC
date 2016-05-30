/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
//import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import sinc.hinc.common.API.HINCManagementAPI;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.global.API.ResourcesManagementAPIImpl;
import sinc.hinc.global.API.HINCManagementImpl;
import sinc.hinc.model.API.ResourcesManagementAPI;

/**
 *
 * @author hungld
 */
public class testMessage {

    public static void main(String[] args) throws Exception {
        /**
         * This part connect the client to the message queue, get the list of DElise
         */
    	ResourcesManagementAPI api = new ResourcesManagementAPIImpl();
        HINCManagementAPI mngAPI = new HINCManagementImpl();
        mngAPI.setHINCGlobalMeta(new HINCGlobalMeta("default", "amqp://localhost", "amqp"));
        mngAPI.queryHINCLocal(2000);

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(mngAPI.queryHINCLocal(2000)));

     
        // test similar will RESTcall
        String hincGlobalEndpoint = "http://localhost:8888/global-management-service-1.0/rest";
        
//        ResourcesManagementAPI rest = (ResourcesManagementAPI) JAXRSClientFactory.create(hincGlobalEndpoint, ResourcesManagementAPI.class, Collections.singletonList(new JacksonJsonProvider()));
//        System.out.println("service date: "+rest.health());

    }
}
