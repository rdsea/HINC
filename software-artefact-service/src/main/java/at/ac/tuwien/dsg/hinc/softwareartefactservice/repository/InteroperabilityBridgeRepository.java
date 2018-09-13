package at.ac.tuwien.dsg.hinc.softwareartefactservice.repository;

import org.springframework.stereotype.Repository;
import sinc.hinc.common.model.InteroperabilityBridge;
import sinc.hinc.common.model.SoftwareArtefact;
import sinc.hinc.repository.DAO.AbstractDAO;

@Repository
public class InteroperabilityBridgeRepository extends AbstractDAO<InteroperabilityBridge> {
    public InteroperabilityBridgeRepository() {
        super(InteroperabilityBridge.class);
    }
}
