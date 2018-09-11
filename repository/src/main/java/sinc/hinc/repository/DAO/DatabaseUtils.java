/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DAO;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.ResourceProvider;


/**
 *
 * @author hungld
 */
public class DatabaseUtils {

    static Class[] clazzes = {Resource.class, ResourceProvider.class};
    private static Logger logger = LoggerFactory.getLogger(DatabaseUtils.class.getName());

    public static void initDB() {
        logger.debug("Initilizing the database... any data will be cleaned!");
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();

        try {

            for (Class clazz : clazzes) {
                String className = clazz.getSimpleName();
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
            }

        } finally {
            manager.closeConnection();
        }
        logger.debug("Initilizing the database complete!");
    }
}
