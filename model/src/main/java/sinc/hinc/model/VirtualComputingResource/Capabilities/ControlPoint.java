/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource.Capabilities;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import sinc.hinc.model.VirtualComputingResource.Capability;

/**
 * The class manage control point, which is an action on the resource. Because
 * the control need actual to execute something, it show clear way to invoke the
 * capability
 *
 * @author hungld
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class ControlPoint extends Capability {

    public static enum InvokeProtocol {
        GET, POST, DELETE, PUT,
        LOCAL_EXECUTE
    }
    /**
     * How to call the capability
     */
    InvokeProtocol invokeProtocol;

    /**
     * The absolute path to the service, e.g. http://example.com/rest/start/{id}
     * Note: The parameters are put in brackets
     */
    String reference;

    /**
     * parameter can be the parameter of local execution or parameter of REST
     * Note that, the parameters can be marshalled in different model via JSON in the case of REST
     * 
     */
    String parameters;

//    Map<String, Object> controlStates;
    /**
     * **************
     * GETER/SETTER * **************
     */
    public ControlPoint() {

    }

    public ControlPoint(String resourceID, String name, String description) {
        super(resourceID, name, description);
//        this.uuid = gatewayID + "/" + resourceID + "/" + name;
    }

    public ControlPoint(String resourceID, String name, String description, InvokeProtocol invokeProtocol, String reference) {
        super(resourceID, name, description);
        this.invokeProtocol = invokeProtocol;
        this.reference = reference;
//        this.uuid = gatewayID + "/" + resourceID + "/" + name;
    }

    public InvokeProtocol getInvokeProtocol() {
        return invokeProtocol;
    }

    public void setInvokeProtocol(InvokeProtocol invokeProtocol) {
        this.invokeProtocol = invokeProtocol;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

}
