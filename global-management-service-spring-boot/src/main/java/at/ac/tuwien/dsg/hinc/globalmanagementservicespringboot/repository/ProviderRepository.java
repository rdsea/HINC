package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository;

import org.springframework.stereotype.Repository;
import sinc.hinc.common.model.ResourceProvider;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

@Repository
public class ProviderRepository extends AbstractDAO<ResourceProvider> {
    public ProviderRepository() {
        super(ResourceProvider.class);
    }
}
