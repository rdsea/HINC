/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.repository.DAO.orientDB;

import at.ac.tuwien.dsg.hinc.repository.DTOMapper.DTOMapperInterface;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.DTOMapperInterface;
import at.ac.tuwien.dsg.hinc.repository.DAO.orientDB.OrientDBConnector;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.MapperFactory;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
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
    }

    public ODocument save(T object) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        try {
            ODocument odoc = mapper.toODocument(object);
            System.out.println("Saving odoc object: " + odoc.toJSON());
            ODocument result = db.save(odoc);
            System.out.println("Save done: " + odoc.toJSON());
            return result;
        } finally {
            manager.closeConnection();
        }        
    }

    public T delete(T object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public T read(String uuid) {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        try {
//            for (ODocument doc : db.browseClass("SoftwareDefinedGateway")) {
//                if (doc.field("uuid").equals(uuid)){
//                    return mapper.fromODocument(doc);
//                }                
//            }
            
            String query = "SELECT * FROM "+className+" WHERE uuid = '" + uuid + "'";
            System.out.println("I will execute a query: " + query);
            List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(query));
            if (!result.isEmpty()) {
                ODocument doc = result.get(0);
                System.out.println("Read odoc JSON:" + doc.toJSON());
                return mapper.fromODocument(doc);
            }
        } finally {
            db.close();
        }
        return null;
    }

    public List<SoftwareDefinedGateway> readAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
