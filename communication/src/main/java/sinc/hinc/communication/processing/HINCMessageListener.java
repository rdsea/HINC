/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.communication.processing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.factory.MessageSubscribeInterface;

/**
 *
 * @author hungld
 */
public class HINCMessageListener {

    static Logger logger = LoggerFactory.getLogger(HINCMessageListener.class);
    MessageClientFactory FACTORY;

    public HINCMessageListener(String broker, String brokerType) {
        FACTORY = new MessageClientFactory(broker, brokerType);
    }

    private class Handler {

        String topic;
        String messageType;
        HINCMessageHander handlerMethod;

        public Handler(String topic, String messageType, HINCMessageHander handlerMethod) {
            this.topic = topic;
            this.messageType = messageType;
            this.handlerMethod = handlerMethod;
        }
    }

    List<Handler> handlers = new ArrayList<>();

    public void addListener(String topic, String messageType, HINCMessageHander handler) {
        logger.debug("Adding a listener -- message: {}, topic: {}, handler: {}", messageType, topic, handler.getClass().getSimpleName());
        handlers.add(new Handler(topic, messageType, handler));
    }

    private HINCMessageHander getHandlerMethod(String topic, String messageType) {
        for (Handler handler : handlers) {
            if (handler.topic.equals(topic) && handler.messageType.equals(messageType)) {
                return handler.handlerMethod;
            }
        }
        return null;
    }

    public void listen() {
        Set<String> topicSet = new HashSet<>();
        // search for all topics which are not duplicated
        for (Handler handler : this.handlers) {
            topicSet.add(handler.topic);
        }

        for (String topic : topicSet) {
            MessageSubscribeInterface subscribeClientBroadCast = FACTORY.getMessageSubscriber(new HINCMessageHander() {
                @Override
                public void handleMessage(HincMessage msg) {
                    String msgTopic = msg.getTopic();
                    String msgType = msg.getMsgType();
                    HINCMessageHander handlerMethod = getHandlerMethod(msgTopic, msgType);
                    if (handlerMethod != null) {
                        logger.debug("Get message: {}, topic: {}. Found a handler: {}", msgType, msgTopic, handlerMethod.getClass().getSimpleName());
                        handlerMethod.handleMessage(msg);
                    } else {
                        logger.error("Get message: {}, topic: {}, but NO handler is found !", msgType, msgTopic);
                    }
                }
            }); // end new HINC Message Handling

            subscribeClientBroadCast.subscribe(topic);
            logger.debug("Listener is listening on the topic: {}", topic);
        }

    }

}
