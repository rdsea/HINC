package sinc.hinc.common.model.payloads;

import org.springframework.data.annotation.Id;
import sinc.hinc.common.model.capabilities.ControlPoint;

public class ControlResult {

    public enum ControlResultOutcome{
        SUCCESSFUL,
        FAILED,
    }

    private ControlResultOutcome outcome;
    private String rawOutput;
    @Id
    private String controlUuid;
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ControlResultOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(ControlResultOutcome outcome) {
        this.outcome = outcome;
    }

    public String getRawOutput() {
        return rawOutput;
    }

    public void setRawOutput(String rawOutput) {
        this.rawOutput = rawOutput;
    }

    public String getControlUuid() {
        return controlUuid;
    }

    public void setControlUuid(String controlUuid) {
        this.controlUuid = controlUuid;
    }

}
