package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.model.LocalMS;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocalMSRepository extends MongoRepository<LocalMS, String> {
}
