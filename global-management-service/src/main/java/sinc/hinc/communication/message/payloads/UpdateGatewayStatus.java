/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.communication.message.payloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 *
 * @author hungld
 */
public class UpdateGatewayStatus {

    List<IoTUnit> appear = new ArrayList<>();
    List<IoTUnit> disappear = new ArrayList<>();
    long timeStamp;

    public UpdateGatewayStatus() {
    }

    public List<IoTUnit> getAppear() {
        return appear;
    }

    public void setAppear(List<IoTUnit> appear) {
        this.appear = appear;
    }

    public List<IoTUnit> getDisappear() {
        return disappear;
    }

    public void setDisappear(List<IoTUnit> disappear) {
        this.disappear = disappear;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static UpdateGatewayStatus fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, UpdateGatewayStatus.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
