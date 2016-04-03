package at.ac.tuwien.dsg.hinc.repository.DAO.orientDB;

import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Capability;
import static at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.CapabilityType.ControlPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.DTOMapperInterface;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.MapperFactory;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hungld
 */
public class SoftwareDefinedGatewayDAO extends AbstractDAO<SoftwareDefinedGateway> {

    public SoftwareDefinedGatewayDAO() {
        super(SoftwareDefinedGateway.class);
    }

    @Override
    public ODocument save(SoftwareDefinedGateway gw) {
//        AbstractDAO<DataPoint> dpDAO = new AbstractDAO<>(DataPoint.class);
//        AbstractDAO<ControlPoint> cpDAO = new AbstractDAO<>(ControlPoint.class);
//        AbstractDAO<CloudConnectivity> ccDAO = new AbstractDAO<>(CloudConnectivity.class);
//        AbstractDAO<ExecutionEnvironment> eeDAO = new AbstractDAO<>(ExecutionEnvironment.class);
//        AbstractDAO gwDAO = new AbstractDAO(SoftwareDefinedGateway.class);

        long time1 = (new Date()).getTime();
        System.out.println("Start saving gateway to DB: " + gw.getUuid());
        DTOMapperInterface<DataPoint> dpMapper = MapperFactory.getMapper(DataPoint.class);
        DTOMapperInterface<ControlPoint> cpMapper = MapperFactory.getMapper(ControlPoint.class);
        DTOMapperInterface<CloudConnectivity> ccMapper = MapperFactory.getMapper(CloudConnectivity.class);
        DTOMapperInterface<ExecutionEnvironment> eeMapper = MapperFactory.getMapper(ExecutionEnvironment.class);
        DTOMapperInterface<SoftwareDefinedGateway> gwMapper = MapperFactory.getMapper(SoftwareDefinedGateway.class);

        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection(); 
        
        ODocument gatewayDB = db.save(gwMapper.toODocument(gw));
        
        List<ODocument> docs = new ArrayList<>();
        

        for (Capability capa : gw.getCapabilities()) {
            switch (capa.getCapabilityType()) {
                case DataPoint:
                    docs.add(dpMapper.toODocument((DataPoint) capa));
                    break;
                case ControlPoint:
                    docs.add(cpMapper.toODocument((ControlPoint) capa));
                    break;
                case CloudConnectivity:
//                    ccDAO.save((CloudConnectivity)capa);
                    break;
                case ExecutionEnvironment:
//                    eeDAO.save((ExecutionEnvironment)capa);
                    break;
            }
        }
        long time2 = (new Date()).getTime();                
               
        try {
            System.out.println("Start to save all oDoc: " + docs.size() +" items");
            for (ODocument odoc : docs) {
                String uuid = odoc.field("uuid");
                ODocument existed = null;

                // Search for exist record
                if (db.getMetadata().getSchema().existsClass(className)) {
                    String query1 = "SELECT * FROM " + className + " WHERE uuid = '" + uuid + "'";
//                    System.out.println("I will execute a query: " + query1);
                    List<ODocument> existed_items = db.query(new OSQLSynchQuery<ODocument>(query1));
                    if (!existed_items.isEmpty()) {
                        existed = existed_items.get(0);
                    }
                }
                                // merge or create new
                if (uuid != null && existed != null) {
                    existed.merge(odoc, true, false);
//                    System.out.println("Merging and saving odoc object: " + existed.toJSON());
                     db.save(existed);
                } else {
//                    System.out.println("Saving odoc object: " + odoc.toJSON());
                     db.save(odoc);
                }
//                System.out.println("Save done: " + result.toJSON());
            }
        } finally {
            manager.closeConnection();
        }
        long time3 = (new Date()).getTime();
        System.out.println("Convert time to oDoc take: " + (((double)time2-(double)time1)/1000) + " seconds");
        System.out.println("Saving to DB done, take: " + (((double)time3-(double)time2)/1000) + " seconds");
        System.out.println("Tocal time take for DB saving DB: " + (((double)time3-(double)time1)/1000) + " seconds");
        return gatewayDB;
    }

    @Override
    public List<ODocument> saveAll(List<SoftwareDefinedGateway> objects) {
        if (objects == null) {
            return null;
        }
        List<ODocument> docs = new ArrayList<>();
        for (SoftwareDefinedGateway gw : objects) {
            docs.add(save(gw));
        }
        return docs;
    }
}
