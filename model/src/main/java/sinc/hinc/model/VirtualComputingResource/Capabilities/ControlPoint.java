/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource.Capabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import sinc.hinc.model.API.HINCPersistable;

/**
 * The class manage control point, which is an action on the resource. Because
 * the control need actual to execute something, it show clear way to invoke the
 * capability
 *
 * @author hungld
 */
public class ControlPoint implements HINCPersistable {

    public static enum InvokeProtocol {
        GET, POST, DELETE, PUT,
        LOCAL_EXECUTE,
        MOCK
    }

    public static enum ControlType {
        CONNECT_TO_NETWORK,
        SELF_CONFIGURE,
        PROVISION,
    }
    // uuid to point directly to the control. We use it for identifying in DB also
    String iotUnitID;

    String name;
    /**
     * Metadata of the control points - controlType: the control is for what
     * purpose. - condition: the condition to enable the control (e.g. this
     * control can configure sensor to MQTT broker only) - effect: do not use
     * this time
     */
    ControlType controlType;
    // condition now support state style only, not range or greater/lesser
    Map<String, String> conditions;
    // effect is to change state of the unit. This will be add into the "state"
    // this will be automatic change the "state" of the unit after the 
    //   control point is executed successfully
    // the changing is from HINC
    Map<String, String> effects;

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
    Map<String, String> parameters = new HashMap<>();

    public ControlPoint() {
    }

    public ControlPoint(String name, InvokeProtocol invokeProtocol, String reference) {
        this.name = name;
        this.invokeProtocol = invokeProtocol;
        this.reference = reference;
        this.controlType = ControlType.SELF_CONFIGURE;
    }

    public ControlPoint(String name, InvokeProtocol invokeProtocol, String reference, ControlType controlType) {
        this.name = name;
        this.invokeProtocol = invokeProtocol;
        this.reference = reference;
        this.controlType = controlType;
    }
    
    // the iotUnitUUID can be used for provider UUID. It can be set by HINC or manually set.
    public ControlPoint belongTo(String providerOrUnitUUID){
        this.iotUnitID = providerOrUnitUUID;
        return this;
    }

    // to have this field only for Jackson to work properly
    String uuid;

    @Override
    public String getUuid() {
        this.uuid = this.iotUnitID + "/" + name;
        return this.uuid;
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

    public void setControlType(ControlType controlType) {
        this.controlType = controlType;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getConditions() {
        return conditions;
    }

    public Map<String, String> getEffects() {
        return effects;
    }

    public void setConditions(Map<String, String> conditions) {
        this.conditions = conditions;
    }

    public void setEffects(Map<String, String> effects) {
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String iotUnitResourceID() {
        return iotUnitID.substring(iotUnitID.indexOf("/") + 1);
    }

    public ControlPoint hasCondition(String key, String value) {
        if (this.conditions == null) {
            this.conditions = new HashMap<>();
        }
        this.conditions.put(key, value);
        return this;
    }

    public ControlPoint hasEffect(String key, String value) {
        if (this.effects == null) {
            this.effects = new HashMap<>();
        }
        this.effects.put(key, value);
        return this;
    }

    public ControlType getControlType() {
        return controlType;
    }

    public String getIotUnitID() {
        return iotUnitID;
    }

    public void setIotUnitID(String iotUnitID) {
        this.iotUnitID = iotUnitID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.iotUnitID);
        hash = 37 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ControlPoint other = (ControlPoint) obj;
        if (!Objects.equals(this.iotUnitID, other.iotUnitID)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

}
