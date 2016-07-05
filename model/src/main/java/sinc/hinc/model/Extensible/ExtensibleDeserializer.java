/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.Extensible;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import sinc.hinc.model.Extensible.reading.MQTTReading;

/**
 *
 * @author hungld
 */
public class ExtensibleDeserializer extends JsonDeserializer<ExtensibleModel> {

    @Override
    public ExtensibleModel deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
//        try {
//            
//            String classPath = jp.getAsJsonObject().getAsJsonPrimitive("clazz").getAsString();
//            Class<MyInterface> cls = (Class<MyInterface>) Class.forName(classPath);
//
//            return (MyInterface) context.deserialize(json, cls);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return null;

        System.out.println("DESERIALIZINGGGGGGGGGGGGGG");

        System.out.println(jp.readValueAsTree().get("clazz").toString());
        System.out.println("Is class correct??");
        
        System.out.println("Inside the deserialize: " + jp.readValueAsTree().toString());
        String json = "{\"clazz\":\"sinc.hinc.model.Extensible.reading.MQTTReading\",\"broker\":\"endpoint\",\"topic\":\"theTopic\"}";

        ExtensibleModel ext = ExtensibleModel.fromJson(jp.readValueAsTree().toString());
        System.out.println("Convert done");
        System.out.println("After convert: " + ext.writeToJson());
        return ext;
    }

    public static void main(String[] args) throws IOException {
//        String json="{\"clazz\":\"sinc.hinc.model.Extensible.reading.MQTTReading\",\"broker\":\"tcp://localhost:1883\",\"topic\":\"mysensor1234\"}";
        ObjectMapper mapper = new ObjectMapper();

        MQTTReading reading = new MQTTReading("endpoint", "theTopic");
        System.out.println(mapper.writeValueAsString(reading));
        System.out.println("================");
        String json = "{\"clazz\":\"sinc.hinc.model.Extensible.reading.MQTTReading\",\"broker\":\"endpoint\",\"topic\":\"theTopic\"}";

        ExtensibleModel ext = ExtensibleModel.fromJson(json);
        System.out.println(ext);
        System.out.println(ext.getClazz().toString());
        System.out.println(((MQTTReading) ext).getBroker());

//        ExtensibleModel model = (ExtensibleModel)mapper.readValue(json, reading.getClazz());
//        System.out.println(model);
//        System.out.println(model.getClazz().getName());
    }

}
