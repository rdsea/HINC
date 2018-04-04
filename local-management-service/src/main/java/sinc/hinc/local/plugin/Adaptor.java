package sinc.hinc.local.plugin;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.HincMessage;
import sinc.hinc.communication.factory.MessageClientFactory;

import java.util.Map;

public class Adaptor {

    private Map<String, String> settings;
    private String name;
    private String exchange;

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

    public void pushMessage(HincMessage message){
        MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker());
        FACTORY.getMessagePublisher().pushMessage(message);
    }

    public void scanResources(){
        String routingKey = settings.get("routingKey");
        HincMessage message = new HincMessage(
                HINCMessageType.QUERY_RESOURCES.toString(),
                HincConfiguration.getMyUUID(),
                this.exchange ,
                "", // TODO add routing key
                null);

        message.setRoutingKey(routingKey);
        message.setExchangeType("direct");
        this.pushMessage(message);
    }

    public void scanResourceProvider(){
        // TODO
    }
}
