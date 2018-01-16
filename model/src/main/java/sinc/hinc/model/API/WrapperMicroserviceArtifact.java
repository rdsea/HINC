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
import sinc.hinc.model.SoftwareArtifact.MicroserviceArtifact;

/**
 *
 * @author hungld
 */
public class WrapperMicroserviceArtifact {

    List<MicroserviceArtifact> mServices = new ArrayList<>();

    public WrapperMicroserviceArtifact() {
    }

    public WrapperMicroserviceArtifact(List<MicroserviceArtifact> services) {
        this.mServices = services;
    }

    public WrapperMicroserviceArtifact(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            WrapperMicroserviceArtifact wrapper = mapper.readValue(json, WrapperMicroserviceArtifact.class);
            this.mServices = wrapper.getmServices();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<MicroserviceArtifact> getmServices() {
        return mServices;
    }

    public void setmServices(List<MicroserviceArtifact> mServices) {
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
