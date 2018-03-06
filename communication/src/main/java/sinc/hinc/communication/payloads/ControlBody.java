package sinc.hinc.communication.payloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class ControlBody {
    private Map<String, String> parameters;
    private String controlPointUUID;
    private String adaptorName;


    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getControlPointUUID() {
        return controlPointUUID;
    }

    public void setControlPointUUID(String controlPointUUID) {
        this.controlPointUUID = controlPointUUID;
    }

    public String getAdaptorName() {
        return adaptorName;
    }

    public void setAdaptorName(String adaptorName) {
        this.adaptorName = adaptorName;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    public static ControlBody fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, ControlBody.class);
        } catch (IOException ex) {
            return null;
        }
    }
}
