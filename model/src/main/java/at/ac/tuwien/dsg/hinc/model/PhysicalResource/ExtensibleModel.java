/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.model.PhysicalResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * This class wrap domain model in order to capture later and persist in DB
 *
 * @author hungld
 */
public class ExtensibleModel {

    Class clazz;
    Object object;

    public ExtensibleModel(Class clazz, Object object) {
        this.clazz = clazz;
        this.object = object;
    }

    public Object readFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException ex) {
            return null;
        }
    }

    public String writeToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

}
