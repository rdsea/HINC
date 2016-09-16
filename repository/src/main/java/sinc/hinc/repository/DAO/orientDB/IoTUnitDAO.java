package sinc.hinc.repository.DAO.orientDB;

import com.orientechnologies.orient.core.record.impl.ODocument;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ExecutionEnvironment;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hungld
 */
public class IoTUnitDAO extends AbstractDAO<IoTUnit> {

    AbstractDAO<DataPoint> datapointDAO = new AbstractDAO<>(DataPoint.class);
    AbstractDAO<ControlPoint> controlpointDAO = new AbstractDAO<>(ControlPoint.class);
    AbstractDAO<CloudConnectivity> connectivityDAO = new AbstractDAO<>(CloudConnectivity.class);
    AbstractDAO<ExecutionEnvironment> executionEnvironmentDAO = new AbstractDAO<>(ExecutionEnvironment.class);
    AbstractDAO<IoTUnit> iotUnitDAO = new AbstractDAO(IoTUnit.class);

    public IoTUnitDAO() {
        super(IoTUnit.class);
    }

    @Override
    public ODocument save(IoTUnit unit) {

        long time1 = (new Date()).getTime();
        System.out.println("Start saving IoT Unit to DB: " + unit.getUuid());

        long time2 = (new Date()).getTime();
        ODocument unitDocObject = iotUnitDAO.save(unit);
        System.out.println("///////////////////////////////////");
        System.out.println("Saving IoTUnit only done: " + unitDocObject.toJSON());
        System.out.println("///////////////////////////////////");

        datapointDAO.saveAll(unit.getDatapoints());
        controlpointDAO.saveAll(unit.getControlpoints());

        long time3 = (new Date()).getTime();
        System.out.println("Convert time to oDoc take: " + (((double) time2 - (double) time1) / 1000) + " seconds");
        System.out.println("Saving to DB done, take: " + (((double) time3 - (double) time2) / 1000) + " seconds");
        System.out.println("Tocal time take for DB saving DB: " + (((double) time3 - (double) time1) / 1000) + " seconds");
        return unitDocObject;
    }

    @Override
    public List<ODocument> saveAll(Collection<IoTUnit> objects) {
        if (objects == null) {
            return null;
        }
        List<ODocument> docs = new ArrayList<>();
        for (IoTUnit gw : objects) {
            docs.add(save(gw));
        }
        return docs;
    }

    @Override
    public IoTUnit read(String uuid) {
        IoTUnit gw = (IoTUnit) iotUnitDAO.read(uuid);
        // read all the capability which have gatewayUUID
        String whereCondition = "iotunituuid='" + uuid + "'";
        List<DataPoint> dps = datapointDAO.readWithCondition(whereCondition);
        List<ControlPoint> cps = controlpointDAO.readWithCondition(whereCondition);
        if (dps != null) {
            gw.getDatapoints().addAll(dps);
        }
        if (cps != null) {
            gw.getControlpoints().addAll(cps);
        }
        return gw;
    }

    @Override
    public List<IoTUnit> readAll() {
        List<IoTUnit> units = iotUnitDAO.readAll();
        for (IoTUnit unit : units) {
            String whereCondition = "iotunituuid='" + unit.getUuid() + "'";
            List<DataPoint> dps = datapointDAO.readWithCondition(whereCondition);
            List<ControlPoint> cps = controlpointDAO.readWithCondition(whereCondition);
            if (dps != null) {
                unit.setDatapoints(new HashSet<DataPoint>(dps));
            }
            if (cps != null) {
                unit.setControlpoints(new HashSet<ControlPoint>(cps));
            }
        }
        return units;
    }
}
