/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.repository.DAO.orientDB;

import at.ac.tuwien.dsg.hinc.repository.DTOMapper.DTOMapperInterface;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.MapperFactory;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.exception.OCommandExecutionException;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hungld
 */
public class AbstractDAO<T> {

    DTOMapperInterface<T> mapper;
    String className;

    public AbstractDAO(Class clazz) {
        mapper = MapperFactory.getMapper(clazz);
        this.className = clazz.getSimpleName();
        if (mapper == null) {
            System.out.println("No mapper for class " + className + " is found. Error!");
        }
    }

    public ODocument save(T object) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        try {
            ODocument odoc = mapper.toODocument(object);
            String uuid = odoc.field("uuid");
            ODocument existed = null;

            // Search for exist record
            if (db.getMetadata().getSchema().existsClass(className)) {
                String query1 = "SELECT * FROM " + className + " WHERE uuid = '" + uuid + "'";
                System.out.println("I will execute a query: " + query1);
                List<ODocument> existed_items = db.query(new OSQLSynchQuery<ODocument>(query1));
                if (!existed_items.isEmpty()) {
                    existed = existed_items.get(0);
                }
            }
            ODocument result;
            // merge or create new
            if (uuid != null && existed != null) {
                existed.merge(odoc, true, false);
                System.out.println("Merging and saving odoc object: " + existed.toJSON());
                result = db.save(existed);
            } else {
                System.out.println("Saving odoc object: " + odoc.toJSON());
                result = db.save(odoc);
            }
            System.out.println("Save done: " + result.toJSON());
            return result;
        } finally {
            manager.closeConnection();
        }
    }

    public List<ODocument> saveAll(List<T> objects) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        List<ODocument> result = new ArrayList<>();
        boolean checked = false; // only check 1 time
        try {
            System.out.println("Prepare to save " + objects.size() + " items");
            for (T obj : objects) {
                ODocument odoc = mapper.toODocument(obj);
                String uuid = odoc.field("uuid");
                ODocument existed = null;

                // Search for exist record                
                if (!checked && db.getMetadata().getSchema().existsClass(className)) {
                    String query1 = "SELECT * FROM " + className + " WHERE uuid = '" + uuid + "'";
                    System.out.println("I will execute a query: " + query1);
                    List<ODocument> existed_items = db.query(new OSQLSynchQuery<ODocument>(query1));
                    if (!existed_items.isEmpty()) {
                        existed = existed_items.get(0);
                    }
                    checked = true;
                }

                // save new or update it
                if (uuid != null && existed != null) {
                    existed.merge(odoc, true, false);
                    ODocument r = db.save(existed);
                    result.add(r);
                    System.out.println("Merging and saving done odoc object: " + existed.toJSON());
                } else {
                    ODocument r = db.save(odoc);
                    result.add(r);
                    System.out.println("Saving done for odoc object: " + odoc.toJSON());
                }
                return result;
            }
            System.out.println("Save done: " + result.size() + " ODocument(s)");
            return result;
        } finally {
            manager.closeConnection();
        }
    }

    public T delete(T object) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        if (db.getMetadata().getSchema().existsClass(className)) {
            try {
                ODocument odoc = mapper.toODocument(object);
                System.out.println("Deleting odoc object: " + odoc.toJSON());
                String uuid = odoc.field("uuid");
                String command = "DELETE FROM " + className + " WHERE uuid = '" + uuid + "'";
                System.out.println("I will execute a query: " + command);
                db.command(new OCommandSQL(command)).execute();
                return object;
            } finally {
                manager.closeConnection();
            }
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
                System.out.println("I will execute a query: " + command);
                db.command(new OCommandSQL(command)).execute();
            } finally {
                manager.closeConnection();
            }
        }

        return null;
    }

    public T read(String uuid) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        try {
            String query = "SELECT * FROM " + className + " WHERE uuid = '" + uuid + "'";
            System.out.println("I will execute a query: " + query);
            List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(query));
            if (!result.isEmpty()) {
                ODocument doc = result.get(0);
                System.out.println("Read odoc JSON:" + doc.toJSON());
                return mapper.fromODocument(doc);
            }
        } finally {
            manager.closeConnection();
        }
        return null;
    }

//    private ODocument readOdocKeepConnection(String uuid) {
//        OrientDBConnector manager = new OrientDBConnector();
//        ODatabaseDocumentTx db = manager.getConnection();
//
//        String query = "SELECT * FROM " + className + " WHERE uuid = '" + uuid + "'";
//        System.out.println("I will execute a query: " + query);
//        List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(query));
//        if (!result.isEmpty()) {
//            return result.get(0);
//        }
//
//        return null;
//    }
    public List<T> readAll() {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        try {
            String query = "SELECT * FROM " + className;
            System.out.println("I will execute a query: " + query);
            List<ODocument> oResult = db.query(new OSQLSynchQuery<ODocument>(query));

            List<T> tResult = new ArrayList<>();
            for (ODocument o : oResult) {
                tResult.add(mapper.fromODocument(o));
            }
            return tResult;
        } finally {
            manager.closeConnection();
        }
    }
}
