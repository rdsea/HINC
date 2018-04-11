package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.model.LocalMS;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LocalMSRepository extends MongoRepository<LocalMS, String> {
    List<LocalMS> findByGroup(String group);
}
