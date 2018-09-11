/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DAO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;

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
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.print.Doc;

/**
 *
 * @author hungld
 * @param <T> The type of object to be persisted in DB
 */
public class AbstractDAO<T> {

    protected ObjectMapper mapper;
    protected Class<T> clazz;
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    protected MongoTemplate mongoTemplate;
    protected T object;
    protected String collection;

    public AbstractDAO(){
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public AbstractDAO(Class clazz) {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.clazz = clazz;
        this.collection = clazz.getSimpleName();
    }

    public T save(T object) {
        Object idValue = getIdValue(object);


        try {
            Document document = Document.parse(mapper.writeValueAsString(object));
            if(idValue != null) {
                document.put("_id", idValue);
                document = merge(document, idValue);
            }
            mongoTemplate.save(document, collection);
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
        DeleteResult deleteResult = mongoTemplate.remove(object, collection);

        if(deleteResult.getDeletedCount()==0){
            return null;
        }

        return object;
    }

    public void deleteAll(){
        mongoTemplate.dropCollection(clazz.getSimpleName());
    }


    public T read(String uuid) {
        Document d = mongoTemplate.findById(uuid, Document.class, collection);
        return deserializeDocument(d);
    }

    public List<T> readAll(int limit) {
        Query query = new Query();
        query.limit(limit);
        return deserializeDocuments(mongoTemplate.find(query,Document.class, collection));
    }
    
    public List<T> readAll(){
        return deserializeDocuments(mongoTemplate.findAll(Document.class, collection));
    }

    public List<T> query(String jsonQuery, int limit){
        Document document = Document.parse(jsonQuery);

        // if there is uuid field, we need to change it to _id for mongodb
        if(document.containsKey("uuid")){
            document.append("_id", document.get("uuid"));
            document.remove("uuid");
        }
        BasicQuery query = new BasicQuery(document.toJson());
        query.limit(limit);
        List<T> results = mongoTemplate.find(query, clazz, collection);
        return results;
    }

    public List<T> query(String jsonQuery){
        Document document = Document.parse(jsonQuery);

        // if there is uuid field, we need to change it to _id for mongodb
        if(document.containsKey("uuid")){
            document.append("_id", document.get("uuid"));
            document.remove("uuid");
        }
        BasicQuery query = new BasicQuery(document.toJson());
        List<Document> docs = mongoTemplate.find(query, Document.class);

        return deserializeDocuments(docs);
    }

    public void delete(String uuid){
        Query query = new Query(Criteria.where("_id").is(uuid));
        mongoTemplate.remove(query, collection);
        logger.info("deleted record"+uuid);
    }



    protected List<T> deserializeDocuments(List<Document> documents){
        List<T> returnlist = new ArrayList<>();
        for(Document d: documents){
            T object = deserializeDocument(d);
            if(object != null) {
                returnlist.add(object);
            }
        }
        return returnlist;
    }

    protected T deserializeDocument(Document d){
        try {
            T object = mapper.readValue(d.toJson(),clazz);
            setIdField(object,d);

            return object;
        } catch (IOException e) {
            return null;
        }
    }

    protected Object getIdValue(T object){
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

    protected void setIdField(T object, Document document){
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

    protected Document merge(Document newDoc, Object id){
        Document oldDoc = mongoTemplate.findById(id, Document.class, collection);
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
