package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sinc.hinc.common.metadata.HincLocalMeta;

public interface HincLocalMetaRepository extends MongoRepository<HincLocalMeta, String> {
}
