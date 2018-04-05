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
package sinc.hinc.common.communication;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class define type of messages between GLOBAL and LOCAL resource manager
 *
 * @author Duc-Hung Le
 */
public class HincMessage {

    private HINCMessageType msgType;
    private String senderID;
    private String receiverID; // = null or empty for broadcast
    private String payload;
    private long timeStamp;
    private String uuid;
    private HincMessageDestination destination;
    private HincMessageDestination reply;

    public HincMessage() {}

    /**
     * Construct a HINC Message with full information for the sender and
     * listener
     *
     * @param msgType
     * @param senderid
     * @param payload
     */
    public HincMessage(HINCMessageType msgType, String senderid, String payload) {
        this.msgType = msgType;
        this.senderID = senderid;
        this.payload = payload;
        this.timeStamp = System.currentTimeMillis();
        this.uuid = UUID.randomUUID().toString().substring(0, 6);
    }

    /**
     * Construct a HINC Message with random feedback topic and empty payload
     *
     * @param msgType
     * @param senderid
     */
    public HincMessage(HINCMessageType msgType, String senderid) {
        this(msgType, senderid,"");
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


    public HINCMessageType getMsgType() {
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

    public String getPayload() {
        return payload;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setMsgType(HINCMessageType msgType) {
        this.msgType = msgType;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return this.toJson();
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

    public void setDestination(String exchange, String routingKey){
        this.destination = new HincMessageDestination(exchange, routingKey);
    }

    public void setReply(String exchange, String routingKey){
        this.reply = new HincMessageDestination(exchange, routingKey);
    }

    public HincMessageDestination getDestination(){
        return this.destination;
    }

    public HincMessageDestination getReply(){
        return this.reply;
    }



    public class HincMessageDestination{
        private String exchange;
        private String routingKey;

        public HincMessageDestination() {};

        public HincMessageDestination(String exchange, String routingKey){
            this.exchange = exchange;
            this.routingKey = routingKey;
        }

        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        public String getRoutingKey() {
            return routingKey;
        }

        public void setRoutingKey(String routingKey) {
            this.routingKey = routingKey;
        }
    }
}
