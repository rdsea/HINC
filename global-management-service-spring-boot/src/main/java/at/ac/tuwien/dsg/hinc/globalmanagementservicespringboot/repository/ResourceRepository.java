package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository;

import org.springframework.stereotype.Repository;
import sinc.hinc.common.model.Resource;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

@Repository
public class ResourceRepository extends AbstractDAO<Resource> {
    public ResourceRepository() {
        super(Resource.class);
    }
}
