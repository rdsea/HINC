/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.communication.payloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 *
 * @author hungld
 */
public class ControlResult {

    public enum CONTROL_RESULT {
        SUCCESS,
        COMMAND_EXIT_ERROR,
        COMMAND_NOT_FOUND,
        EXECUTOR_NOT_SUPPORT, // e.g. RESTful is not implement yet
        EXECUTOR_ERROR
    }
    CONTROL_RESULT result;
    int exitcode;
    String output;
    long executionTime;
    IoTUnit updateIoTUnit;

    public ControlResult() {
    }

    public ControlResult(CONTROL_RESULT result, int exitcode, String output) {
        this.result = result;
        this.exitcode = exitcode;
        this.output = output;
    }

    public void setResult(CONTROL_RESULT result) {
        this.result = result;
    }

    public void setExitcode(int exitcode) {
        this.exitcode = exitcode;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public IoTUnit getUpdateIoTUnit() {
        return updateIoTUnit;
    }

    public void setUpdateIoTUnit(IoTUnit updateIoTUnit) {
        this.updateIoTUnit = updateIoTUnit;
    }

    public CONTROL_RESULT getResult() {
        return result;
    }

    public int getExitcode() {
        return exitcode;
    }

    public String getOutput() {
        return output;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public ControlResult hasExecutionTime(long time) {
        this.executionTime = time;
        return this;
    }

    public ControlResult hasUnitToUpdate(IoTUnit unit) {
        this.updateIoTUnit = unit;
        return this;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    public static ControlResult fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, ControlResult.class);
        } catch (IOException ex) {
            return null;
        }
    }
}
