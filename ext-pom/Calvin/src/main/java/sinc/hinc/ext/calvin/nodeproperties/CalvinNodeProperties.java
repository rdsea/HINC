/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.ext.calvin.nodeproperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hungld
 */
public class CalvinNodeProperties {

    @JsonProperty("public")
    Map<String, String> publicProp;

    @JsonProperty("private")
    Map<String, String> privateProp;

    @JsonProperty("indexed_public")
    IndexedPublic indexed_public;

    public Map<String, String> getPublicProp() {
        return publicProp;
    }

    public void setPublicProp(Map<String, String> publicProp) {
        this.publicProp = publicProp;
    }

    public Map<String, String> getPrivateProp() {
        return privateProp;
    }

    public void setPrivateProp(Map<String, String> privateProp) {
        this.privateProp = privateProp;
    }

    public IndexedPublic getIndexed_public() {
        return indexed_public;
    }

    public void setIndexed_public(IndexedPublic indexed_public) {
        this.indexed_public = indexed_public;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static CalvinNodeProperties fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, CalvinNodeProperties.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static CalvinNodeProperties fromJsonFile(File file) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(file, CalvinNodeProperties.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
