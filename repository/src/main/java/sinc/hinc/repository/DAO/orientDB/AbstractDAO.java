/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DAO.orientDB;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import org.bson.BSON;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.print.Doc;

/**
 *
 * @author hungld
 * @param <T> The type of object to be persisted in DB
 */
public class AbstractDAO<T> {

    private ObjectMapper mapper;
    private Class<T> clazz;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private MongoTemplate mongoTemplate;
    private T object;

    public AbstractDAO(){
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public AbstractDAO(Class clazz) {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.clazz = clazz;
    }

    public T save(T object) {
        Object idValue = getIdValue(object);


        try {
            Document document = Document.parse(mapper.writeValueAsString(object));
            if(idValue != null) {
                document.put("_id", idValue);
                document = merge(document, idValue);
            }
            mongoTemplate.save(document, clazz.getSimpleName());
            if(idValue == null){
                setIdField(object,document);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //mongoTemplate.save(object, clazz.getSimpleName());

        return object;
    }

    public List<T> saveAll(Collection<T> objects) {
        for(T o : objects){
            save(o);
        }
        return new ArrayList<>(objects);
    }

    public T delete(T object) {
        DeleteResult deleteResult = mongoTemplate.remove(object, clazz.getSimpleName());

        if(deleteResult.getDeletedCount()==0){
            return null;
        }

        return object;
    }

    public void deleteAll(){
        mongoTemplate.dropCollection(clazz.getSimpleName());
    }


    public T read(String uuid) {
        Document d = mongoTemplate.findById(uuid, Document.class, clazz.getSimpleName());
        return deserializeDocument(d);
    }

    public List<T> readAll(int limit) {
        Query query = new Query();
        query.limit(limit);
        return deserializeDocuments(mongoTemplate.find(query,Document.class, clazz.getSimpleName()));
    }
    
    public List<T> readAll(){
        return deserializeDocuments(mongoTemplate.findAll(Document.class, clazz.getSimpleName()));
    }

    private List<T> deserializeDocuments(List<Document> documents){
        List<T> returnlist = new ArrayList<>();
        for(Document d: documents){
            T object = deserializeDocument(d);
            if(object != null) {
                returnlist.add(object);
            }
        }
        return returnlist;
    }

    private T deserializeDocument(Document d){
        try {
            T object = mapper.readValue(d.toJson(),clazz);
            setIdField(object,d);

            return object;
        } catch (IOException e) {
            return null;
        }
    }

    private Object getIdValue(T object){
        Object idValue = new Object();
        for(Field f : object.getClass().getDeclaredFields()){
            if(f.getAnnotation(Id.class) != null){
                boolean accessible = f.isAccessible();
                f.setAccessible(true);
                try {
                    idValue = f.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                f.setAccessible(accessible);
            }
        }
        return idValue;
    }

    private void setIdField(T object, Document document){
        for(Field f : object.getClass().getDeclaredFields()){
            if(f.getAnnotation(Id.class) != null){
                boolean accessible = f.isAccessible();
                f.setAccessible(true);
                try {
                    Object id = document.get("_id");

                    if(id instanceof ObjectId){
                        id = id.toString();
                    }
                    f.set(object,id);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                f.setAccessible(accessible);
            }
        }
    }

    private Document merge(Document newDoc, Object id){
        Document oldDoc = mongoTemplate.findById(id, Document.class, clazz.getSimpleName());
        Document merged = new Document();

        if(oldDoc==null) {
            return newDoc;
        }

        for (String key : newDoc.keySet()) {
            Object value = newDoc.get(key);
            if (newDoc.get(key) == null) {
                merged.put(key, oldDoc.get(key));
            } else {
                merged.put(key, value);
            }
        }


        return merged;

    }
}
