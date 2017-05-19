/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers;

import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.ApplicationMessageType;
import sinc.hinc.local.Main;

/**
 * The payload of the message should have 2 Strings: datapointID time
 *
 * @author hungld
 */
public class SubscribeDataHandler implements HINCMessageHander {

    @Override
    public HincMessage handleMessage(HincMessage message) {
        // these are printed on the Local Controller
        System.out.println("TODO: CHECK if the data is available, then forward data to the public MQTT.");
        System.out.println("TODO: The public MQTT should be a parameter sent by the BCS");

        // add a forwardingDatapoint
        if (message.getPayload() == null || message.getPayload().isEmpty()) {
            HincMessage replyMsg = new HincMessage(ApplicationMessageType.ACK.toString(), HincConfiguration.getMyUUID(), message.getFeedbackTopic(), "", "You must provide DatapointID and duration for the subscription");
            return replyMsg;
        }
        String[] param = message.getPayload().trim().split(" ");
        String sensorID = param[0];

        long duration = 0;
        if (param.length > 1) {
            try {
                duration = Long.parseLong(param[1]);
            } catch (NumberFormatException ex) {
                HincMessage replyMsg = new HincMessage(ApplicationMessageType.ACK.toString(), HincConfiguration.getMyUUID(), message.getFeedbackTopic(), "", "Cannot subscribe. Duration must be numeric.");
                return replyMsg;
            }
        }
        // TODO: implement callback function to FORWARD data. This should based on the Datapoint.dataApi and Datapoint.dataApiSettings
        //Main.LISTENER.forwardDatapoint(sensorID, duration);

        // send a reply to BCS
        String replyStr = "Sensor " + sensorID + " is forwarding. Duration: " + duration;
        HincMessage replyMsg = new HincMessage(ApplicationMessageType.ACK.toString(), HincConfiguration.getMyUUID(), message.getFeedbackTopic(), "", replyStr);
        MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
        FACTORY.getMessagePublisher().pushMessage(replyMsg);
        return replyMsg;
    }

}
