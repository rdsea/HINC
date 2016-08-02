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

    public static enum ControlType {
        CONNECT_TO_NETWORK,
        SELF_CONFIGURE
    }

    /**
     * Metadata of the control points - controlType: the control is for what
     * purpose. - condition: the condition to enable the control (e.g. this
     * control can configure sensor to MQTT broker only) - effect: do not use
     * this time
     */
    ControlType controlType;
    String condition;
    String effect;

    /**
     * How to call the capability - invokeProtocol: the way to invoke: REST or
     * command - reference: the URI or command name - parameter: a set of input
     * of the command. This will be filled on the invocation. the parameters can
     * be marshalled in different model via JSON in REST call The absolute path
     * to the service, e.g. http://example.com/rest/start/{id} Note: The
     * parameters are put in brackets
     *
     */
    InvokeProtocol invokeProtocol;
    String reference;
    String parameters = "";

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

    public void setControlType(ControlType controlType) {
        this.controlType = controlType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public ControlType getControlType() {
        return controlType;
    }

}
