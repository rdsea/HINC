package at.ac.tuwien.dsg.hinc.globalmanagementservice.repository;

import at.ac.tuwien.dsg.hinc.globalmanagementservice.model.LocalMS;
import org.springframework.stereotype.Repository;
import sinc.hinc.repository.DAO.AbstractDAO;

@Repository
public class LocalMSRepository extends AbstractDAO<LocalMS> {
    public LocalMSRepository() {
        super(LocalMS.class);
    }

    // TODO
    // List<LocalMS> findByGroup(String group);
}
