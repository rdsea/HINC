/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.transformer.iotivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author hungld
 */
public class HearRate extends Core {
    public enum HeartZone {
        Zone1,Zone2,Zone3,Zone4,Zone5
    }
    HeartZone zone;
    String value;
    String range;

    public HeartZone getZone() {
        return zone;
    }

    public void setZone(HeartZone zone) {
        this.zone = zone;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
    
    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args){
        HearRate hr = new HearRate();
        hr.setId("1234");
        hr.setName("heartrate");
        hr.setObservable(0);
        hr.setType("heartrate_Sensor");
        hr.setZone(HearRate.HeartZone.Zone1);
        
        System.out.println(hr.toJson());
    }
}
