/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.fiwarecontextbroker.model;

import sinc.hinc.model.PhysicalResource.ExtensibleModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hungld
 */
public class ContextResponse {

    private ContextElement contextElement;

    private StatusCode statusCode;

    public ContextResponse() {

    }

    public ContextElement getContextElement() {
        return contextElement;
    }

    public void setContextElement(ContextElement contextElement) {
        this.contextElement = contextElement;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
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
    
    public static ContextResponse fromJson(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, ContextResponse.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
