/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teit.enumactuator.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author trang
 */
public class EnumActuator {

    private String name;
    private String type;
    private String actuatorJar;
    private Map<String, String> context = new HashMap<>();
    private Map<Integer, EnumControl> controls = new HashMap<>();

    public EnumActuator() {
    }

    public EnumActuator(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public enum INVOKE_RESULT {
        CONTROL_NOT_FOUND, CONDITION_VIOLATED, SUCCESSFUL
    }

    public EnumActuator hasControl(int id, EnumControl control) {
        this.controls.put(id, control);
        return this;
    }

    public EnumControl findControlByName(String controlName) {
        for (EnumControl control : controls.values()) {
            if (control.getName().trim().equals(controlName)) {
                return control;
            }
        }
        return null;
    }

    public EnumControl findControlByID(int id) {
        return controls.get(id);
    }

    // check if the current context violates the condition
    public boolean checkCondition(Map<String, String> condition) {
        for (Map.Entry<String, String> entry : condition.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (context.get(key) != null && !context.get(key).equals(value)) {
                return false;
            }
        }
        return true;
    }

    public INVOKE_RESULT invoke(String actionName, String[] parameter) {
        EnumControl control = findControlByName(actionName);
        if (control == null) {
            return INVOKE_RESULT.CONTROL_NOT_FOUND;
        }
        if (!checkCondition(control.getConditions())) {
            return INVOKE_RESULT.CONDITION_VIOLATED;
        }

        context.putAll(control.getEffects());
        return INVOKE_RESULT.SUCCESSFUL;
    }

    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public boolean toJSONFile(File file) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, this);
            return true;
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static EnumActuator fromJsonFile(File file) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(file, EnumActuator.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static EnumActuator fromJsonString(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, EnumActuator.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    ////////////////////////// GET / SET ///////
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public String contextString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(context);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public Map<Integer, EnumControl> getControls() {
        return controls;
    }

    public void setControls(Map<Integer, EnumControl> controls) {
        this.controls = controls;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActuatorJar() {
        return actuatorJar;
    }

    public void setActuatorJar(String actuatorJar) {
        this.actuatorJar = actuatorJar;
    }

}
