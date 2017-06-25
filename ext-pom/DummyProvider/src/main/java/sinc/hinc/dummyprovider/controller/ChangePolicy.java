/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * How Dummy provider generate metadata
 *
 * Case 1: fix number of sensor, but metadata change - unitMax = unitMin = 100 -
 *
 * @author hungld
 */
public class ChangePolicy {

    static Logger logger = LoggerFactory.getLogger("Dummy");
    int unitMax = 20;
    int unitMin = 10;
    int numberOfChange = 3;
    CHANGE_TYPE changeType = CHANGE_TYPE.METADATA_CHANGE;
    int frequency = 10;

    // ampq and group enable DummyProvider send data when having update
    String amqp;
    String amqpgroup;

    public static void main(String[] args) {
        // a test
        ChangePolicy cp = new ChangePolicy();
        cp.changeType = CHANGE_TYPE.SENSOR_IN_OUT;
        logger.debug(cp.toJson());
    }

    public static enum CHANGE_TYPE {
        SENSOR_IN_OUT, METADATA_CHANGE
    }

    public static ChangePolicy fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, ChangePolicy.class);
        } catch (IOException ex) {
            return null;
        }
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    public int getUnitMax() {
        return unitMax;
    }

    public void setUnitMax(int unitMax) {
        this.unitMax = unitMax;
    }

    public int getUnitMin() {
        return unitMin;
    }

    public void setUnitMin(int unitMin) {
        this.unitMin = unitMin;
    }

    public int getNumberOfChange() {
        return numberOfChange;
    }

    public void setNumberOfChange(int numberOfChange) {
        this.numberOfChange = numberOfChange;
    }

    public CHANGE_TYPE getChangeType() {
        return changeType;
    }

    public void setChangeType(CHANGE_TYPE changeType) {
        this.changeType = changeType;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getAmqp() {
        return amqp;
    }

    public void setAmqp(String amqp) {
        this.amqp = amqp;
    }

    public String getAmqpgroup() {
        return amqpgroup;
    }

    public void setAmqpgroup(String amqpgroup) {
        this.amqpgroup = amqpgroup;
    }

}
