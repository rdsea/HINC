package sinc.hinc.local.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import sinc.hinc.common.communication.HincMessage;
import java.util.Map;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.model.payloads.Control;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.communication.AdaptorCommunicationManager;
import sinc.hinc.common.model.capabilities.ControlPoint;

public class Adaptor {

    private Map<String, String> settings;
    private String name;
    private String exchange;

    public Adaptor(){
        this.exchange = AdaptorCommunicationManager.getInstance().getExchange();
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void scanResources(){
        String routingKey = settings.get("routingKey");
        HincMessage message = new HincMessage(
                HINCMessageType.QUERY_RESOURCES,
                HincConfiguration.getMyUUID(),
                "");

        message.setDestination(AdaptorCommunicationManager.getInstance().getExchange(), routingKey);
        message.setReply(AdaptorCommunicationManager.getInstance().getExchange(), AdaptorCommunicationManager.getInstance().getRoutingKey());


        AdaptorCommunicationManager.getInstance().sendMessage(message);
    }

    public void scanResourceProvider(){
        String routingKey = settings.get("routingKey");
        HincMessage message = new HincMessage(
                HINCMessageType.QUERY_PROVIDER,
                HincConfiguration.getMyUUID(),
                "");

        message.setDestination(AdaptorCommunicationManager.getInstance().getExchange(), routingKey);
        message.setReply(AdaptorCommunicationManager.getInstance().getExchange(), AdaptorCommunicationManager.getInstance().getRoutingKey());


        AdaptorCommunicationManager.getInstance().sendMessage(message);
    }

    public void sendControl(Control control){
        String routingKey = settings.get("routingKey");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            String payload = objectMapper.writeValueAsString(control);

            HincMessage message = new HincMessage(
                    HINCMessageType.CONTROL,
                    HincConfiguration.getMyUUID(),
                    payload);

            message.setDestination(AdaptorCommunicationManager.getInstance().getExchange(), routingKey);
            message.setReply(AdaptorCommunicationManager.getInstance().getExchange(), AdaptorCommunicationManager.getInstance().getRoutingKey());
            AdaptorCommunicationManager.getInstance().sendMessage(message);
        } catch (JsonProcessingException e) {
            // TODO log
            e.printStackTrace();
        }
    }
}
