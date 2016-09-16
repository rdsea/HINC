/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teit.enumactuator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author trang
 */
public class EnumControl {

    private String name;
    private List<String> parameters;

    // replace for startState, endState. Now multiple states can be recorded.
    Map<String, String> conditions = new HashMap<>();
    Map<String, String> effects = new HashMap<>();

    // the command to really execute
    String command;

    public EnumControl() {
    }

    public EnumControl(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnumControl(String name, String command) {
        this.name = name;
        this.command = command;
    }

    public void setParameter(List<String> parameter) {
        this.parameters = parameter;
    }

    public EnumControl hasCondition(String name, String value) {
        this.conditions.put(name, value);
        return this;
    }

    public EnumControl hasEffect(String name, String value) {
        this.effects.put(name, value);
        return this;
    }

    // GET SET
    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, String> conditions) {
        this.conditions = conditions;
    }

    public Map<String, String> getEffects() {
        return effects;
    }

    public void setEffects(Map<String, String> effects) {
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
