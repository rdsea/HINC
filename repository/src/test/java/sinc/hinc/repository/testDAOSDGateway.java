/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository;

import sinc.hinc.repository.DAO.orientDB.AbstractDAO;
import sinc.hinc.repository.DAO.orientDB.OrientDBConnector;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 *
 * @author hungld
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testDAOSDGateway {

    @Test
    public void test1_dropDB() {
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();
        db.drop();
    }

    @Test
    public void test2_SaveIoTUnit() {
        IoTUnit unit = new IoTUnit();
        unit.setHincID("hincEntity123");
        unit.setResourceID("myResourceID-123");

        DataPoint dp = new DataPoint("temp123", "temperature");
        unit.hasDatapoint(dp);

        // write
        AbstractDAO<IoTUnit> gwDAO = new AbstractDAO<>(IoTUnit.class);
        ODocument docgw1 = gwDAO.save(unit);
        System.out.println("Test saving done: " + docgw1.toJSON());
        // read        
        Assert.assertNotNull(docgw1);
    }

    @Test
    public void test3_ReadSDG() {
        AbstractDAO<IoTUnit> gwDAO = new AbstractDAO<>(IoTUnit.class);
        IoTUnit gw1 = gwDAO.read("myUUID");
        System.out.println("READ a IoT unit done: " + gw1.getResourceID());
        Assert.assertEquals("myResourceID-123", gw1.getResourceID());
    }
}
