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
package sinc.hinc.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import sinc.hinc.common.metadata.HINCMessageType;

/**
 * This class define type of messages between GLOBAL and LOCAL resource manager
 *
 * @author Duc-Hung Le
 */
public class HincMessage {

    String msgType;

    String topic;

    String senderID;

    String receiverID; // = null or empty for broadcast

    String feedbackTopic;

    String payload;

    long timeStamp;

    String uuid;

    String routingKey = "";

    String exchangeType;

    Map<String, String> extra;

    private HINCMessageType hincMessageType;
    private String group;

    public HincMessage() {
    }

    /**
     * Construct a HINC Message with full information for the sender and
     * listener
     *
     * @param msgType
     * @param senderid
     * @param topic
     * @param feedbackTopic
     * @param payload
     */
    public HincMessage(String msgType, String senderid, String topic, String feedbackTopic, String payload) {
        this.msgType = msgType;
        this.senderID = senderid;
        this.topic = topic;
        this.feedbackTopic = feedbackTopic;
        this.payload = payload;
        this.timeStamp = System.currentTimeMillis();
        this.uuid = UUID.randomUUID().toString().substring(0, 6);
    }

    /**
     * Construct a HINC Message with random feedback topic and empty payload
     *
     * @param msgType
     * @param senderid
     * @param topic
     */
    public HincMessage(String msgType, String senderid, String topic) {
        this.msgType = msgType;
        this.senderID = senderid;
        this.topic = topic;
        this.feedbackTopic = "sinc.hinc.temp." + UUID.randomUUID();
        this.payload = "";
        this.timeStamp = System.currentTimeMillis();
        this.uuid = UUID.randomUUID().toString().substring(0, 6);
    }

    public HincMessage hasType(HINCMessageType type) {
        this.msgType = type.toString();
        return this;
    }

    public HincMessage hasSenderID(String senderID) {
        this.senderID = senderID;
        return this;
    }

    public HincMessage hasTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public HincMessage hasFeedbackTopic(String feedbackTopic) {
        this.feedbackTopic = feedbackTopic;
        return this;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public HincMessage hasPayload(String payload) {
        this.payload = payload;
        return this;
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

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getMsgType() {
        return msgType;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
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

    public void setMsgType(String msgType) {
        this.msgType = msgType;
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

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "HINCMessage{" + "MsgType=" + msgType + ", payload=" + payload + '}';
    }

    public HINCMessageType getHincMessageType() {
        return hincMessageType;
    }


    public void setHincMessageType(HINCMessageType hincMessageType) {
        this.hincMessageType = hincMessageType;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
