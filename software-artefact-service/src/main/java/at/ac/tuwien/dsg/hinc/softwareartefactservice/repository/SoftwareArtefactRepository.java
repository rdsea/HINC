package at.ac.tuwien.dsg.hinc.softwareartefactservice.repository;

import org.springframework.stereotype.Repository;
import sinc.hinc.common.model.SoftwareArtefact;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

@Repository
public class SoftwareArtefactRepository extends AbstractDAO<SoftwareArtefact> {
    public SoftwareArtefactRepository() {
        super(SoftwareArtefact.class);
    }
}
