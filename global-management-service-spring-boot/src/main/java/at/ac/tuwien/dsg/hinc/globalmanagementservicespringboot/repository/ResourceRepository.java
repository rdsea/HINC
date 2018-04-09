package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sinc.hinc.common.model.Resource;

public interface ResourceRepository extends MongoRepository<Resource, String> {
}
