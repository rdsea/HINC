package at.ac.tuwien.dsg.hinc.globalmanagementservice.repository;

import org.springframework.stereotype.Repository;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

@Repository
public class HincLocalMetaRepository extends AbstractDAO<HincLocalMeta>{
    public HincLocalMetaRepository() {
        super(HincLocalMeta.class);
    }
}
