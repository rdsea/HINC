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
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hungld
 * @param <T> The type of object to be persisted in DB
 */
public class AbstractDAO<T> {

    ObjectMapper mapper;
    String className;
    Class<T> clazz;
    static Logger logger = LoggerFactory.getLogger("HINC");

    public AbstractDAO(Class clazz) {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.className = clazz.getSimpleName();
        this.clazz = clazz;
        // if class name is not existed, create one
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        try {
            if (!db.getMetadata().getSchema().existsClass(className)) {
                logger.debug("Class: " + className + " does not existed, now create it...");
                db.getMetadata().getSchema().createClass(className);

                db.getMetadata().getSchema().getClass(className).createProperty("uuid", OType.STRING);
                db.getMetadata().getSchema().getClass(className).createIndex("uuidIndex_" + className, OClass.INDEX_TYPE.NOTUNIQUE_HASH_INDEX, "uuid");

                if (!db.getMetadata().getSchema().existsClass(className)) {
                    logger.debug("Cannot create class: " + className + ", an error of persistent could happen later!");
                } else {
                    logger.debug("Class " + className + " is created sucessfully !");
                }
            }
        } finally {
            manager.closeConnection();
        }
    }

    public ODocument save(T object) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        try {
            ODocument odoc = new ODocument();
            odoc.setClassName(className);
            odoc.fromJSON(mapper.writeValueAsString(object));
            String uuid = odoc.field("uuid");
            ODocument existed = null;

            // Search for exist record
            if (db.getMetadata().getSchema().existsClass(className)) {
                String query1 = "SELECT * FROM " + className + " WHERE uuid = '" + uuid + "'";
                List<ODocument> existed_items = db.query(new OSQLSynchQuery<ODocument>(query1));
                logger.debug("Query: " + query1 + ". Result: " + existed_items.size());
                if (!existed_items.isEmpty()) {
                    existed = existed_items.get(0);
                }
            } else {
                logger.debug("No class exist: " + className);
            }
            ODocument result;
            // merge or create new
            if (uuid != null && existed != null) {
                existed.merge(odoc, true, false);
                logger.trace("Merging and saving odoc object: " + existed.toJSON());
                result = db.save(existed);
            } else {
                logger.trace("Saving odoc object: " + odoc.toJSON());
                result = db.save(odoc);
            }
            logger.debug("Save object done: " + uuid);
            logger.trace("Save done: " + result.toJSON());
            return result;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            manager.closeConnection();
        }
        return null;
    }

    public List<ODocument> saveAll(Collection<T> objects) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        List<ODocument> result = new ArrayList<>();

        try {
            String taskID = UUID.randomUUID().toString();
            Long startTime = (new Date()).getTime();
            logger.debug("Prepare to save " + objects.size() + " items -- " + taskID);
            for (T obj : objects) {
                ODocument odoc = new ODocument();
                odoc.fromJSON(mapper.writeValueAsString(obj));
                odoc.setClassName(className);
                logger.trace("Adaptor done, obj is: " + odoc.toJSON());
                String uuid = odoc.field("uuid");
                logger.debug("Ok, now saving item with uuid = " + uuid);
                ODocument existed = null;

                // Search for exist record                
                String query1 = "SELECT * FROM " + className + " WHERE uuid = '" + uuid + "'";
                List<ODocument> existed_items = db.query(new OSQLSynchQuery<ODocument>(query1));
                logger.debug("Query: " + query1 + ". Result: " + existed_items.size());
                if (!existed_items.isEmpty()) {
                    logger.trace("There is " + existed_items.size() + " existing item with id: " + uuid);
                    for (ODocument eee : existed_items) {
                        logger.trace("  --> Existed item: " + eee.toJSON());
                        existed = eee;
                    }
//                        existed = existed_items.get(0);
                } else {
                    logger.trace("There is NO existing item with id: " + uuid);
                }

                // save new or update it
                if (uuid != null && existed != null) {
                    existed.merge(odoc, true, false);
                    ODocument r = db.save(existed);
                    result.add(r);
                    logger.trace("Merging and saving done odoc object: " + r.toJSON());
                } else {
                    ODocument r = db.save(odoc);
                    result.add(r);
                    logger.trace("Saving done for odoc object: " + r.toJSON());
                }
                String query2 = "SELECT * FROM " + className + " WHERE uuid = '" + uuid + "'";
                List<ODocument> existed_itemsRequery = db.query(new OSQLSynchQuery<ODocument>(query2));
                logger.trace("  --> Save done, " + existed_itemsRequery.size() + " existed items");
                for (ODocument oo : existed_itemsRequery) {
                    System.out.println("  ----> Item is: " + oo.toJSON());
                }
                System.out.println("==================================");

            }
            Long endTime = (new Date()).getTime();
            logger.debug("Save done: " + result.size() + " ODocument(s) in " + (endTime - startTime) + " milisecs -- " + taskID);
            return result;
        } catch (Exception e) {
            logger.error("Error when saving object to DB", e);
            return null;
        } finally {
            manager.closeConnection();
        }
    }

    public T delete(T object) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        if (db.getMetadata().getSchema().existsClass(className)) {
            try {
                ODocument odoc = new ODocument();
                odoc.fromJSON(mapper.writeValueAsString(object));
                logger.debug("Deleting odoc object: " + odoc.toJSON());
                String uuid = odoc.field("uuid");
                String command = "DELETE FROM " + className + " WHERE uuid = '" + uuid + "'";
                logger.debug("I will execute a query: " + command);
                db.command(new OCommandSQL(command)).execute();
                return object;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } finally {
                manager.closeConnection();
            }
        } else {
            logger.debug("No class exist: " + className);
        }
        return null;
    }

    // delete all items of the class
    public T deleteAll() {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        if (db.getMetadata().getSchema().existsClass(className)) {
            try {
                String command = "DELETE FROM " + className;
                logger.debug("I will execute a query: " + command);
                db.command(new OCommandSQL(command)).execute();
            } finally {
                manager.closeConnection();
            }
        } else {
            logger.debug("No class exist: " + className);
        }

        return null;
    }

    public T read(String uuid) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        logger.trace("Reading DB: " + uuid);

        if (!db.getMetadata().getSchema().existsClass(className)) {
            logger.debug("No class exist: " + className);
            return null;
        }

        try {
            String query = "SELECT * FROM " + className + " WHERE uuid = '" + uuid + "'";
            List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(query));

//            logger.debug("Query: " + query + ". Result: " + result.size());
            if (!result.isEmpty()) {
                ODocument doc = result.get(result.size() - 1);

//                logger.debug("Read odoc JSON, " + result.size() + " items:" + doc.toJSON());
                logger.trace("End reading: " + uuid);
                return mapper.readValue(doc.toJSON(), this.clazz);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            manager.closeConnection();
        }
        return null;
    }

    public List<T> readWithCondition(String whereClause) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        logger.trace("Read DB with condition: " + whereClause);
        if (!db.getMetadata().getSchema().existsClass(className)) {
            logger.debug("No class exist: " + className);
            return null;
        }
        try {
            String query = "SELECT * FROM " + className + " WHERE " + whereClause;
            List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(query));
            logger.trace("Query: " + query + ". Result: " + result.size());
            List<T> convertedResult = new ArrayList<>();
            if (!result.isEmpty()) {
                for (ODocument doc : result) {
//                    logger.debug("Read with conditions:" + doc.toJSON());
//                    logger.debug("END READ CONDITION====================");
                    convertedResult.add( mapper.readValue(doc.toJSON(), this.clazz));
                }
                return convertedResult;
            }
            logger.trace("Read condition done, objects: " + result.size());
            return convertedResult;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            manager.closeConnection();
        }

        return new ArrayList<>();
    }

    /**
     * Read all the instances of the class name
     * @param limit The amount of instances return. Keep limit less than 0 to return all.
     * @return 
     */
    public List<T> readAll(int limit) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        logger.trace("Read all: " + className);
        if (!db.getMetadata().getSchema().existsClass(className)) {
            logger.debug("No class exist: " + className);
            return null;
        }
        try {            
            String query = "SELECT * FROM " + className;
            if (limit > 0){
                query += " LIMIT " + limit;
            }
            List<ODocument> oResult = db.query(new OSQLSynchQuery<ODocument>(query));
//            logger.debug("Query: " + query + ". Result: " + oResult.size());
            List<T> tResult = new ArrayList<>();
            for (ODocument o : oResult) {
                tResult.add(mapper.readValue(o.toJSON(), this.clazz));
            }
            logger.trace("Read all done: " + className + ". Objs: " + tResult.size());
            return tResult;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            manager.closeConnection();
        }

        return new ArrayList<>();
    }
    
    public List<T> readAll(){
        return readAll(-1);
    }
}
