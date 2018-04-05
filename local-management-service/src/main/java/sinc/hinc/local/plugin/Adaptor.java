package sinc.hinc.local.plugin;

import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.communication.AdaptorCommunicationManager;
import sinc.hinc.local.communication.LocalCommunicationManager;

import java.util.Map;

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
                HINCMessageType.QUERY_RESOURCES.toString(),
                HincConfiguration.getMyUUID(),
                "");

        message.setDestination(AdaptorCommunicationManager.getInstance().getExchange(), routingKey);
        message.setReply(AdaptorCommunicationManager.getInstance().getExchange(), AdaptorCommunicationManager.getInstance().getRoutingKey());


        AdaptorCommunicationManager.getInstance().sendMessage(message);
    }

    public void scanResourceProvider(){
        // TODO
    }
}
