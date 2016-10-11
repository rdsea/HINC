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
import sinc.hinc.model.VirtualComputingResource.MicroService;

/**
 *
 * @author hungld
 */
public class WrapperMicroService {

    List<MicroService> mServices = new ArrayList<>();

    public WrapperMicroService() {
    }

    public WrapperMicroService(List<MicroService> services) {
        this.mServices = services;
    }

    public WrapperMicroService(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            WrapperMicroService wrapper = mapper.readValue(json, WrapperMicroService.class);
            this.mServices = wrapper.getmServices();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<MicroService> getmServices() {
        return mServices;
    }

    public void setmServices(List<MicroService> mServices) {
        this.mServices = mServices;
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
