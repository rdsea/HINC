package sinc.hinc.repository.DAO.orientDB;

import sinc.hinc.model.VirtualComputingResource.Capability.Capability;
import static sinc.hinc.model.VirtualComputingResource.Capability.CapabilityType.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import com.orientechnologies.orient.core.record.impl.ODocument;
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

        long time2 = (new Date()).getTime();
        ODocument gatewayDB = gatewayDAO.save(gw);
//        for (DataPoint dp : gw.getDataPoints()) {
//            datapointDAO.save(dp);
//        }
        datapointDAO.saveAll(gw.getDataPoints());
        for (ControlPoint cp : gw.getControlPoints()) {
            controlpointDAO.save(cp);
        }
        for (CloudConnectivity con : gw.getConnectivity()) {
            connectivityDAO.save(con);
        }
        for (ExecutionEnvironment ee : gw.getExecutionEnvironment()) {
            executionEnvironmentDAO.save(ee);
        }

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
            gw.getDataPoints().addAll(dps);
        }
        if (cps != null) {
            gw.getControlPoints().addAll(cps);
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
                gw.getDataPoints().addAll(dps);
            }
            if (cps != null) {
                gw.getControlPoints().addAll(cps);
            }
        }
        return gws;
    }
}
