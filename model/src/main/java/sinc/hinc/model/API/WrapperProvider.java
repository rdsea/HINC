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
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

/**
 *
 * @author hungld
 */
//TODO remove
public class WrapperProvider {

    List<ResourcesProvider> units = new ArrayList<>();

    public WrapperProvider() {
    }

    public WrapperProvider(List<ResourcesProvider> list) {
        this.units.addAll(list);
    }

    public WrapperProvider(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            WrapperProvider wrapper = mapper.readValue(json, WrapperProvider.class);
            this.units = wrapper.getUnits();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<ResourcesProvider> getUnits() {
        return units;
    }

    public void setUnits(List<ResourcesProvider> units) {
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
