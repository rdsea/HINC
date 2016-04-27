package sinc.hinc.repository.DAO.orientDB;

import sinc.hinc.model.VirtualComputingResource.Capability.Capability;
import static sinc.hinc.model.VirtualComputingResource.Capability.CapabilityType.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import sinc.hinc.repository.DTOMapper.MapperFactory;
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

    AbstractDAO<DataPoint> datapointDAO = new AbstractDAO<>(DataPoint.class);
    AbstractDAO<ControlPoint> controlpointDAO = new AbstractDAO<>(ControlPoint.class);
    AbstractDAO<CloudConnectivity> connectivityDAO = new AbstractDAO<>(CloudConnectivity.class);
    AbstractDAO<ExecutionEnvironment> executionEnvironmentDAO = new AbstractDAO<>(ExecutionEnvironment.class);
    AbstractDAO<SoftwareDefinedGateway> gatewayDAO = new AbstractDAO(SoftwareDefinedGateway.class);

    public SoftwareDefinedGatewayDAO() {
        super(SoftwareDefinedGateway.class);
    }

    @Override
    public ODocument save(SoftwareDefinedGateway gw) {

        long time1 = (new Date()).getTime();
        System.out.println("Start saving gateway to DB: " + gw.getUuid());
//        DTOMapperInterface<DataPoint> dpMapper = MapperFactory.getMapper(DataPoint.class);
//        DTOMapperInterface<ControlPoint> cpMapper = MapperFactory.getMapper(ControlPoint.class);
//        DTOMapperInterface<CloudConnectivity> ccMapper = MapperFactory.getMapper(CloudConnectivity.class);
//        DTOMapperInterface<ExecutionEnvironment> eeMapper = MapperFactory.getMapper(ExecutionEnvironment.class);
//        DTOMapperInterface<SoftwareDefinedGateway> gwMapper = MapperFactory.getMapper(SoftwareDefinedGateway.class);
//        OrientDBConnector manager = new OrientDBConnector();
//        ODatabaseDocumentTx db = manager.getConnection(); //        
//        ODocument gatewayDB = db.save(gwMapper.toODocument(gw));        
//        List<ODocument> docs = new ArrayList<>();

        long time2 = (new Date()).getTime();
        ODocument gatewayDB = gatewayDAO.save(gw);

        for (Capability capa : gw.getCapabilities()) {
            switch (capa.getCapabilityType()) {
                case DataPoint:
                    datapointDAO.save((DataPoint) capa);
                    break;
                case ControlPoint:
                    controlpointDAO.save((ControlPoint) capa);
                    break;
                case CloudConnectivity:
//                    ccDAO.save((CloudConnectivity)capa);
                    break;
                case ExecutionEnvironment:
//                    eeDAO.save((ExecutionEnvironment)capa);
                    break;
            }
        }

//        try {
//            System.out.println("Start to save all oDoc: " + docs.size() + " items");
//            for (ODocument odoc : docs) {
//                String uuid = odoc.field("uuid");
//                ODocument existed = null;
//
//                // Search for exist record
//                if (db.getMetadata().getSchema().existsClass(className)) {
//                    String query1 = "SELECT * FROM " + className + " WHERE uuid = '" + uuid + "'";
//                    List<ODocument> existed_items = db.query(new OSQLSynchQuery<ODocument>(query1));
//                    if (!existed_items.isEmpty()) {
//                        existed = existed_items.get(0);
//                    }
//                }
//                // merge or create new
//                if (uuid != null && existed != null) {
//                    existed.merge(odoc, true, false);
////                    System.out.println("Merging and saving odoc object: " + existed.toJSON());
//                    db.save(existed);
//                } else {
////                    System.out.println("Saving odoc object: " + odoc.toJSON());
//                    db.save(odoc);
//                }
////                System.out.println("Save done: " + result.toJSON());
//            }
//        } finally {
//            manager.closeConnection();
//        }
        long time3 = (new Date()).getTime();
        System.out.println("Convert time to oDoc take: " + (((double) time2 - (double) time1) / 1000) + " seconds");
        System.out.println("Saving to DB done, take: " + (((double) time3 - (double) time2) / 1000) + " seconds");
        System.out.println("Tocal time take for DB saving DB: " + (((double) time3 - (double) time1) / 1000) + " seconds");
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

    @Override
    public SoftwareDefinedGateway read(String uuid) {
        SoftwareDefinedGateway gw = (SoftwareDefinedGateway) gatewayDAO.read(uuid);
        // read all the capability which have gatewayUUID
        String whereCondition = "gatewayID='" + uuid + "'";
        List<DataPoint> dps = datapointDAO.readWithCondition(whereCondition);
        List<ControlPoint> cps = controlpointDAO.readWithCondition(whereCondition);
        if (dps != null) {
            gw.getCapabilities().addAll(dps);
        }
        if (cps != null) {
            gw.getCapabilities().addAll(cps);
        }
        return gw;
    }

    @Override
    public List<SoftwareDefinedGateway> readAll() {
        List<SoftwareDefinedGateway> gws = gatewayDAO.readAll();
        for (SoftwareDefinedGateway gw : gws) {
            String whereCondition = "gatewayid='" + gw.getUuid() + "'";
            List<DataPoint> dps = datapointDAO.readWithCondition(whereCondition);
            List<ControlPoint> cps = controlpointDAO.readWithCondition(whereCondition);
            if (dps != null) {
                gw.getCapabilities().addAll(dps);
            }
            if (cps != null) {
                gw.getCapabilities().addAll(cps);
            }
        }
        return gws;
    }
}
