/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import sinc.hinc.abstraction.ResourceDriver.IoTUnitProcessor;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.processing.HincMessage;

/**
 *
 * @author hungld
 */
public class IoTUnitUpdateProcessor {

    String hincUUID;

    public IoTUnitUpdateProcessor(String hincUUID) {
        this.hincUUID = hincUUID;
    }


    public void process(String unit) {

    }

}
