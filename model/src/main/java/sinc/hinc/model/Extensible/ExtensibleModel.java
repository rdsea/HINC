/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.Extensible;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class wrap domain model in order to capture later and persist in DB
 *
 * @author hungld
 */
//@JsonSerialize(using = ExtensibleSerializer.class)
//@JsonDeserialize(using = ExtensibleDeserializer.class)
public class ExtensibleModel  {

    // how can CRUD the data
    Class clazz;
    
    public ExtensibleModel() {
    }

    public ExtensibleModel(Class clazz) {
        this.clazz = clazz;
    }

    public Object readFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ExtensibleModel fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(json);
            System.out.println("Clazz IS: " + node.get("clazz").asText());
            String className = node.get("clazz").asText();
            return (ExtensibleModel)mapper.readValue(json, Class.forName(className));
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String writeToJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Class getClazz() {
        return clazz;
    }

   

}
