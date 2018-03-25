package sinc.hinc.communication.payloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class ControlResourceProviderBody {
    private Map<String, String> parameters;
    private String resouceProviderUuid;
    private String controlPointUuid;
    private String adaptorName;


    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getResouceProviderUuid() {
        return resouceProviderUuid;
    }

    public void setResouceProviderUuid(String resouceProviderUuid) {
        this.resouceProviderUuid = resouceProviderUuid;
    }

    public String getAdaptorName() {
        return adaptorName;
    }

    public void setAdaptorName(String adaptorName) {
        this.adaptorName = adaptorName;
    }

    public String getControlPointUuid() {
        return controlPointUuid;
    }

    public void setControlPointUuid(String controlPointUuid) {
        this.controlPointUuid = controlPointUuid;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    public static ControlResourceProviderBody fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, ControlResourceProviderBody.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
