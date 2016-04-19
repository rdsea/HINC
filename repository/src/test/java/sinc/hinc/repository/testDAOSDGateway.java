/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository;

import sinc.hinc.repository.DAO.orientDB.AbstractDAO;
import sinc.hinc.repository.DAO.orientDB.OrientDBConnector;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import java.util.List;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
    public void test2_SaveSDG() {
        SoftwareDefinedGateway gw = new SoftwareDefinedGateway();
        gw.setName("myGW");
        gw.setUuid("myUUID");

        DataPoint dp = new DataPoint("mySensorID", "datapoint-name", "This is a datapoint");
        gw.hasCapability(dp);

        // write
        AbstractDAO<SoftwareDefinedGateway> gwDAO = new AbstractDAO<>(SoftwareDefinedGateway.class);
        ODocument docgw1 = gwDAO.save(gw);
        System.out.println("Test saving done: " + docgw1.toJSON());
        // read        
        Assert.assertNotNull(docgw1);
    }

    @Test
    public void test3_ReadSDG() {
        AbstractDAO<SoftwareDefinedGateway> gwDAO = new AbstractDAO<>(SoftwareDefinedGateway.class);
        SoftwareDefinedGateway gw1 = gwDAO.read("myUUID");
        System.out.println("READ a gateway done: " + gw1.getName());
        Assert.assertEquals("myGW", gw1.getName());
    }
}
