package sinc.hinc.common.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.annotation.Id;

import java.util.List;

public class InteroperabilityBridge {
    @Id
    private String uuid;

    private List<String> bridgeElements;

    private JsonNode metadata;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public JsonNode getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonNode metadata) {
        this.metadata = metadata;
    }

    public List<String> getBridgeElements() {
        return bridgeElements;
    }

    public void setBridgeElements(List<String> bridgeElements) {
        this.bridgeElements = bridgeElements;
    }
}
