package sinc.hinc.abstraction.ResourceDriver;

import sinc.hinc.model.VirtualComputingResource.IoTUnit;

public class ProviderControlResult {
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

    public ProviderControlResult() {
    }

    public ProviderControlResult(CONTROL_RESULT result, int exitcode, String output) {
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

    public ProviderControlResult hasExecutionTime(long time) {
        this.executionTime = time;
        return this;
    }

    public ProviderControlResult hasUnitToUpdate(IoTUnit unit) {
        this.updateIoTUnit = unit;
        return this;
    }
}
