package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import sinc.hinc.common.model.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResourceRepositoryImpl{
    private MongoDbFactory mongoDbFactory;
    private MongoTemplate mongoTemplate;
    private MappingMongoConverter mappingMongoConverter;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public ResourceRepositoryImpl(MongoDbFactory mongoDbFactory, MappingMongoConverter mappingMongoConverter) {
        this.mongoDbFactory = mongoDbFactory;
        this.mappingMongoConverter = mappingMongoConverter;
        mongoTemplate = new MongoTemplate(mongoDbFactory, mappingMongoConverter);
    }

    public Resource save(Resource resource){
        try {
            String json = objectMapper.writeValueAsString(resource);
            mongoTemplate.save(json, "jsonresource");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resource;
    }

    public List<Resource> findAll(){
        List<String> resourcejson = mongoTemplate.findAll(String.class, "jsonresource");
        List<Resource> resources = new ArrayList<>();
        for(String s:resourcejson){
            try {
                resources.add(objectMapper.readValue(s,Resource.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resources;
    }

    public List<Resource> saveAll(Iterable<Resource> resources){
        List<Resource> resourceList = new ArrayList<>();
        for(Resource r:resources){
            save(r);
            resourceList.add(r);
        }
        return resourceList;
    }
}
