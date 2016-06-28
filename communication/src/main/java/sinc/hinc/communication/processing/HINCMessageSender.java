/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.communication.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.factory.MessagePublishInterface;
import sinc.hinc.communication.factory.MessageSubscribeInterface;

/**
 * The class send messages from HINC Global to multiple HINC Local.
 *
 * @author hungld
 */
public class HINCMessageSender {

    Logger logger = LoggerFactory.getLogger(HINCMessageSender.class);
    MessageClientFactory FACTORY;
//    List<HincLocalMeta> listOfHINCLocal = new ArrayList<>();

    public HINCMessageSender(String broker, String brokerType) {
        this.FACTORY = new MessageClientFactory(broker, brokerType);
    }

    public void asynCall(int timeout, HincMessage requestMessage, HINCMessageHander handler) {
        MessageSubscribeInterface sub = FACTORY.getMessageSubscriber(handler);
        logger.debug("Will subscribe to the topic: " + requestMessage.getFeedbackTopic() + " to wait for the reply");
        sub.subscribe(requestMessage.getFeedbackTopic(), timeout);
        MessagePublishInterface pub = FACTORY.getMessagePublisher();
        pub.pushMessage(requestMessage);
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("Asyn call done on topic {} ! should close the subscribe now.. ", requestMessage.getFeedbackTopic());
    }

    // with a single UUID we don't need timeout. We wait for the message return. The result can be only String
    public String synCall(HincMessage queryMessage) {
        MessagePublishInterface pub = FACTORY.getMessagePublisher();
        logger.debug("Calling the function: " + queryMessage.toJson());
        HincMessage responseMessage = pub.callFunction(queryMessage);
        logger.debug("Function called and return the result: " + responseMessage.getPayload());
        return responseMessage.getPayload();
    }


}
