/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.API;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sinc.hinc.model.VirtualComputingResource.VirtualResource;

/**
 *
 * @author hungld
 */
public class WrapperIoTUnit {

    List<VirtualResource> units = new ArrayList<>();

    public WrapperIoTUnit() {
    }

    public WrapperIoTUnit(List<VirtualResource> list) {
        this.units.addAll(list);
    }

    public WrapperIoTUnit(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            WrapperIoTUnit wrapper = mapper.readValue(json, WrapperIoTUnit.class);
            this.units = wrapper.getUnits();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<VirtualResource> getUnits() {
        return units;
    }

    public void setUnits(List<VirtualResource> units) {
        this.units = units;
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
