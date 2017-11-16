/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource.Capabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
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
        SELF_CONFIGURE
    }
    String uuid = UUID.randomUUID().toString();
    String name;

    // list of virtual resource that the control belongs to
    List<String> resourceUuid = new ArrayList<>();
    Map<String, String> meta = new HashMap<>();

    /**
     * Metadata of the control points.
     * 
     * - controlType: the purpose of the control
     *
     * - condition: the condition to enable the control (e.g. this control can
     * configure sensor to MQTT broker only) - effect: change the metadata if
     * the control is done
     */
    ControlType controlType;
    // condition now support state style only, not range or greater/lesser
    Map<String, String> conditions;
    
    /** Effect is to change state of the unit.
     *  HINC uses this info to update state of the virtual iot resource
     */    
    Map<String, String> effects;

    /**
     * How to call the capability.
     *
     * invokeProtocol: the way to invoke: REST or command reference: the URI or
     * command name parameter: a set of input of the command.
     *
     * This will be filled on the invocation. the parameters can be marshalled
     * in different model via JSON in REST call The absolute path to the
     * service, e.g. http://example.com/rest/start/{id} Note: The parameters are
     * put in brackets
     *
     */
    InvokeProtocol invokeProtocol;
    String reference;
    String parameters = "";

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

    @Override
    public String getUuid() {
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

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public void setControlType(ControlType controlType) {
        this.controlType = controlType;
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

    public List<String> getResourceUuid() {
        return resourceUuid;
    }

    public void setResourceUuid(List<String> resourceUuid) {
        this.resourceUuid = resourceUuid;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.uuid);
        hash = 97 * hash + Objects.hashCode(this.name);
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
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

}
