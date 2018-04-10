package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sinc.hinc.common.model.ResourceProvider;

public interface ProviderRepository extends MongoRepository<ResourceProvider, String> {
}
