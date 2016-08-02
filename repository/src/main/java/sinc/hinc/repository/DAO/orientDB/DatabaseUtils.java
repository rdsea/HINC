/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DAO.orientDB;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ExecutionEnvironment;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.NetworkService;
import static sinc.hinc.repository.DAO.orientDB.AbstractDAO.logger;

/**
 *
 * @author hungld
 */
public class DatabaseUtils {

    static Class[] clazzes = {SoftwareDefinedGateway.class, DataPoint.class, ControlPoint.class, CloudConnectivity.class, ExecutionEnvironment.class, NetworkService.class};

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
