package at.ac.tuwien.dsg.hinc.repository.DAO.orientDB;

import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Capability;
import static at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.CapabilityType.ControlPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import at.ac.tuwien.dsg.hinc.repository.DAO.orientDB.AbstractDAO;
import com.orientechnologies.orient.core.record.impl.ODocument;
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
        AbstractDAO<DataPoint> dpDAO = new AbstractDAO<>(DataPoint.class);
        AbstractDAO<ControlPoint> cpDAO = new AbstractDAO<>(ControlPoint.class);
        AbstractDAO<CloudConnectivity> ccDAO = new AbstractDAO<>(CloudConnectivity.class);
        AbstractDAO<ExecutionEnvironment> eeDAO = new AbstractDAO<>(ExecutionEnvironment.class);
        AbstractDAO gwDAO = new AbstractDAO(SoftwareDefinedGateway.class);

        gwDAO.save(gw);
        for (Capability capa : gw.getCapabilities()) {
            switch (capa.getCapabilityType()) {
                case DataPoint:
                    dpDAO.save((DataPoint) capa);
                    break;
                case ControlPoint:
//                    cpDAO.save((ControlPoint)capa);
                    break;
                case CloudConnectivity:
//                    ccDAO.save((CloudConnectivity)capa);
                    break;
                case ExecutionEnvironment:
//                    eeDAO.save((ExecutionEnvironment)capa);
                    break;
            }
        }
        return null;
    }

    @Override
    public List<ODocument> saveAll(List<SoftwareDefinedGateway> objects) {
        if (objects == null) {
            return null;
        }
        AbstractDAO dpDAO = new AbstractDAO(DataPoint.class);
        AbstractDAO cpDAO = new AbstractDAO(ControlPoint.class);
        AbstractDAO ccDAO = new AbstractDAO(CloudConnectivity.class);
        AbstractDAO eeDAO = new AbstractDAO(ExecutionEnvironment.class);
        AbstractDAO gwDAO = new AbstractDAO(SoftwareDefinedGateway.class);

        for (SoftwareDefinedGateway gw : objects) {
            gwDAO.save(gw);
            for (Capability capa : gw.getCapabilities()) {
                switch (capa.getCapabilityType()) {
                    case DataPoint:
                        dpDAO.save((DataPoint) capa);
                        break;
                    case ControlPoint:
//                        cpDAO.save((ControlPoint)capa);
                        break;
                    case CloudConnectivity:
//                        ccDAO.save((CloudConnectivity)capa);
                        break;
                    case ExecutionEnvironment:
//                        eeDAO.save((ExecutionEnvironment)capa);
                        break;
                }
            }
        }

        return null;
    }
}
