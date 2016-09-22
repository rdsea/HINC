/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.demoapplication.IoT;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HINCMessageListener;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 * This is how application can listen to the update from HINC. The update can
 * come from: Unit or gateway
 *
 * @author hungld
 */
public class ListenToTheChanges {

    public static void main(String[] args) {

        HINCMessageListener listener = null;

        listener = new HINCMessageListener("amqp://localhost", "amqp");
        listener.addListener(HincMessageTopic.getBroadCastTopic("my-group"), HINCMessageType.UPDATE_INFORMATION.toString(), new HINCMessageHander() {
            @Override
            public HincMessage handleMessage(HincMessage message) {
                IoTUnit unit = IoTUnit.fromJson(message.getPayload());
                System.out.println("Got an update about IoT Unit with ID: " + unit.getResourceID());
                return null;
            }
        });
        listener.addListener(HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), HINCMessageType.SYN_REPLY.toString(), new HINCMessageHander() {
            @Override
            public HincMessage handleMessage(HincMessage message) {
                HincLocalMeta meta = HincLocalMeta.fromJson(message.getPayload());
                System.out.println("A new HINC-enabled gateway is discovered: " + meta.getUuid());
                return null;
            }
        });
        listener.listen();

    }
}
