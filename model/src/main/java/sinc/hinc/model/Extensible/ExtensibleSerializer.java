/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.Extensible;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 *
 * @author hungld
 */
public class ExtensibleSerializer extends JsonSerializer<ExtensibleModel> {

    @Override
    public void serialize(ExtensibleModel t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        System.out.println("Inside the serialize: t.writeToJson: " + t.writeToJson());
        jg.writeString(t.writeToJson());
    }

    

}
