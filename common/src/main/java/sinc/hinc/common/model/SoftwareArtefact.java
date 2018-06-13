package sinc.hinc.common.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.annotation.Id;

public class SoftwareArtefact {
    @Id
    private String uuid;

    private String executionEnvironment;
    private String artefactReference;

    private JsonNode metadata;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getExecutionEnvironment() {
        return executionEnvironment;
    }

    public void setExecutionEnvironment(String executionEnvironment) {
        this.executionEnvironment = executionEnvironment;
    }

    public String getArtefactReference() {
        return artefactReference;
    }

    public void setArtefactReference(String artefactReference) {
        this.artefactReference = artefactReference;
    }

    public JsonNode getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonNode metadata) {
        this.metadata = metadata;
    }
}
