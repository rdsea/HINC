package at.ac.tuwien.dsg.hinc.globalmanagementservice;

import at.ac.tuwien.dsg.hinc.globalmanagementservice.repository.ResourceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sinc.hinc.common.model.Resource;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTests {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads() {
    }


    @Test
    public void repositoryTest_saveMergesObjects(){
        Resource resource = new Resource();

        List<Resource> resourceList = resourceRepository.readAll();
        int count = resourceList.size();

        resource.setUuid("newResource");
        resource.setName("testName");
        String originalName = resource.getName();
        resourceRepository.save(resource);

        int count2 = resourceRepository.readAll().size();

        Resource readResource = resourceRepository.read(resource.getUuid());

        String id = readResource.getUuid();

        Resource updatedResource = new Resource();
        updatedResource.setUuid("newResource");
        updatedResource.setLocation("testlocation");

        resourceRepository.save(updatedResource);

        Resource readResource2 = resourceRepository.read(resource.getUuid());
        String name = readResource2.getName();

        resourceRepository.delete(resource);

        int count3 = resourceRepository.readAll().size();

        System.out.println(count + " " + count2 + " " + count3);

        Assert.assertEquals(originalName, name);
        Assert.assertEquals(count+1,count2 );
        Assert.assertEquals(count,count3);
    }

    @Test
    public void repositoryTest_nullId(){
        Resource resource = new Resource();

        List<Resource> resourceList = resourceRepository.readAll();
        int count = resourceList.size();


        resource.setName("resourcewithout uuid");
        resourceRepository.save(resource);

        int count2 = resourceRepository.readAll().size();

        Resource readResource = resourceRepository.read(resource.getUuid());

        String id = readResource.getUuid();

        resourceRepository.delete(resource);

        int count3 = resourceRepository.readAll().size();
        System.out.println(count + " " + count2 + " " + count3);

        Assert.assertNotNull(id);
        Assert.assertEquals(count+1,count2 );
        Assert.assertEquals(count,count3);


    }

    @Test
    public void repositoryTest_jsonNodeSerialization(){
        Resource resource = new Resource();

        resource.setUuid("jsonNode testresource");
        resource.setName("resourcewithout uuid");
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("testfield", "testvalue");
        jsonNode.put("testfield2", 3);
        resource.setMetadata(jsonNode);


        resourceRepository.save(resource);


        Resource readResource = resourceRepository.read(resource.getUuid());


        JsonNode metadata = readResource.getMetadata();


        JsonNode testfield = metadata.get("testfield");
        JsonNode originalField = jsonNode.get("testfield");
        boolean isvalueOriginal = originalField.isValueNode();
        boolean isvalueTest = testfield.isValueNode();

        Assert.assertEquals(isvalueOriginal, isvalueTest);
        Assert.assertEquals(resource.getMetadata(), readResource.getMetadata());


        resourceRepository.delete(resource);


    }

}
