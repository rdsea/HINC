/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.fiwarecontextbroker.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hungld
 */
public class ContextResponseWrapper {

    private List<ContextResponse> contextResponses = new ArrayList<ContextResponse>();

    public List<ContextResponse> getContextResponses() {
        return contextResponses;
    }

    public void setContextResponses(List<ContextResponse> contextResponses) {
        this.contextResponses = contextResponses;
    }
    
    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static ContextResponseWrapper fromJson(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, ContextResponseWrapper.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
