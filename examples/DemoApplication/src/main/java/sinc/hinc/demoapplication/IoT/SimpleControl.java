/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.demoapplication.IoT;

import java.util.ArrayList;
import java.util.List;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HINCMessageSender;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.API.WrapperIoTUnit;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 * Requirement: You need a testbed which including some resources before running this.
 * Please edit the URL of AMQP endpoint to match with your configuration.
 * 
 * @author hungld
 */
public class SimpleControl {

    public static void main(String[] args) {
        
        // FIRST, let us query the IoT Unit around
        HincMessage request = new HincMessage()
                .hasType(HINCMessageType.RPC_QUERY_INFORMATION_GLOBAL)
                .hasSenderID("my-application")
                .hasTopic(HincMessageTopic.getBroadCastTopic("my-group"));

        // SECOND, create a message sender that connect to the communication endpoint, here is the local AMQP
        HINCMessageSender sender = new HINCMessageSender("amqp://localhost", "amqp");

        // THIRD, send a message and pass the information
        final List<IoTUnit> units = new ArrayList<>();
        sender.asynCall(2000, request, new HINCMessageHander() {
            @Override
            public HincMessage handleMessage(HincMessage responseMsg) {
                // extract the IoT Unit in the response message
                String messagePayload = responseMsg.getPayload();
                WrapperIoTUnit wrapper = new WrapperIoTUnit(messagePayload);
                List<IoTUnit> lstUnits = wrapper.getUnits();

                // do something on the new information
                System.out.println("Query " + lstUnits.size() + " units");
                for (IoTUnit unit : lstUnits) {
                    System.out.println(" -- Unit: " + unit.getResourceID());
                }

                // save these unit for later use
                units.addAll(lstUnits);
                return null;
            }
        });

        // FOURTH try to send a control to each resource
        for (IoTUnit unit : units) {
            ControlPoint cp = unit.findControlpointByName("turn-on");
            if (cp != null) {
                HincMessage request2 = new HincMessage()
                        .hasType(HINCMessageType.CONTROL)
                        .hasPayload(cp.getUuid());
                sender.synCall(request2);
            }
        }
    }
}
