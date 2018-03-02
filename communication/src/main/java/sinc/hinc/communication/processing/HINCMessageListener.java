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
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
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

    public class Handler {

        String topic;
        String messageType;
        HINCMessageHander handlerMethod;

        public Handler(String topic, String messageType, HINCMessageHander handlerMethod) {
            this.topic = topic;
            this.messageType = messageType;
            this.handlerMethod = handlerMethod;
        }

        public String getTopic() {
            return topic;
        }

        public String getMessageType() {
            return messageType;
        }

        public HINCMessageHander getHandlerMethod() {
            return handlerMethod;
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

    Set<String> alreadyListening = new HashSet<>();

    public void listen() {
        Set<String> topicSet = new HashSet<>();
        // search for all topics which are not duplicated
        for (Handler handler : this.handlers) {
            topicSet.add(handler.topic);
        }
        topicSet.removeAll(alreadyListening);

        for (String topic : topicSet) {
            MessageSubscribeInterface subscribeClientBroadCast = FACTORY.getMessageSubscriber(new HINCMessageHander() {
                @Override
                public HincMessage handleMessage(HincMessage msg) {
                    try {
                        // check if the message is broadcast or for me
                        if (msg.getReceiverID()!=null && !msg.getReceiverID().isEmpty() && !msg.getReceiverID().equals(HincConfiguration.getMyUUID())){
                            logger.debug("The message " + msg.getMsgType() + " is not for me (" + HincConfiguration.getMyUUID() +"), it is for: " + msg.getReceiverID());
                            return null; // no, it is not a broadcast and not for me, it is for another HINC local
                        }
                        // if the message is for me, process it
                        String msgTopic = msg.getTopic();
                        String msgType = msg.getMsgType();
                        HINCMessageHander handlerMethod = getHandlerMethod(msgTopic, msgType);
                        if (handlerMethod != null) {
                            System.out.println(msg.toJson());
//                        logger.debug("Get message: {}, topic: {}. Found a handler: {}", msgType, msgTopic, handlerMethod.getClass().getSimpleName());
                            logger.debug("Get message: " + msgType + ", topic: " + msgTopic + ", found handler: " + handlerMethod.getClass().getSimpleName());
                            HincMessage replyMsg = handlerMethod.handleMessage(msg);
                            if (replyMsg != null) {
                                MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
                                FACTORY.getMessagePublisher().pushMessage(replyMsg);
                                logger.debug("Handle message {} done and a reply is pushed bash at topic {}", msg.getMsgType(), replyMsg.getTopic());
                            }
                            return replyMsg;
                        } else {
//                        logger.error("Get message: {}, topic: {}, but NO handler is found !", msgType, msgTopic);
                            logger.debug("Get message: " + msgType + "on topic: " + msgTopic + ", but no handler found!");
                            return new HincMessage(HINCMessageType.MISC.toString(), "", "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }); // end new HINC Message Handling

            subscribeClientBroadCast.subscribe(topic);
            alreadyListening.add(topic);
//            logger.debug("Listener is listening on the topic: {}", topic);
            System.out.println("Listener is listening on the topic: " + topic);
        }

    }

    public List<Handler> getHandlers() {
        return handlers;
    }

}
