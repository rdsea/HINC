/*
 * Copyright (c) 2013 Technische Universitat Wien (TUW), Distributed Systems Group. http://dsg.tuwien.ac.at
 *
 * This work was partially supported by the European Commission in terms of the CELAR FP7 project (FP7-ICT-2011-8 #317790), http://www.celarcloud.eu/
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package sinc.hinc.communication.processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * This class define type of messages between GLOBAL and LOCAL resource manager
 *
 * @author Duc-Hung Le
 */
public class HincMessage {

    String msgType;
    
    String topic;

    String senderID;

    String feedbackTopic;

    String payload;

    long timeStamp;

    Map<String, String> extra;

    public HincMessage() {
    }

    public HincMessage(String msgType, String senderid, String topic, String feedbackTopic, String payload) {
        this.msgType = msgType;
        this.senderID = senderid;        
        this.topic = topic;
        this.feedbackTopic = feedbackTopic;
        this.payload = payload;
        this.timeStamp = System.currentTimeMillis();
    }


    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static HincMessage fromJson(byte[] bytes) {
        return fromJson(new String(bytes, StandardCharsets.UTF_8));
    }

    public static HincMessage fromJson(String s) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(s, HincMessage.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getMsgType() {
        return msgType;
    }

    public String getSenderID() {
        return senderID;
    }    
    

    public String getTopic() {
        return topic;
    }

    public String getFeedbackTopic() {
        return feedbackTopic;
    }

    public String getPayload() {
        return payload;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setFeedbackTopic(String feedbackTopic) {
        this.feedbackTopic = feedbackTopic;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void hasExtra(String key, String value) {
        if (extra == null) {
            extra = new HashMap<>();
        }
        extra.put(key, value);
    }

    @Override
    public String toString() {
        return "HINCMessage{" + "MsgType=" + msgType + ", payload=" + payload + '}';
    }

}
