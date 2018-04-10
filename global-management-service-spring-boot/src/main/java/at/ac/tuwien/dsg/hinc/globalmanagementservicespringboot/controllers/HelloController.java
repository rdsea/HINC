package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.controllers;
import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.HincLocalMetaRepository;
import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.repository.ResourceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.model.Resource;

@RestController
@RequestMapping("/test")
public class HelloController {
    @Value("${hinc.global.rabbitmq.input}")
    private String globalInputExchange;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HincLocalMetaRepository hincLocalMetaRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @GetMapping("/")
    public ResponseEntity index() {
        return new ResponseEntity("Greetings from Spring Boot HINC!", HttpStatus.OK);
    }


    @GetMapping("/meta")
    public ResponseEntity test() {
        HincMessage testMessage = new HincMessage();
        testMessage.setMsgType(HINCMessageType.SYN_REPLY);
        testMessage.setSenderID("id");
        testMessage.setDestination("group","group");

        HincLocalMeta hincLocalMeta = new HincLocalMeta();
        hincLocalMeta.setBroker("broker");
        hincLocalMeta.setCity("city");
        hincLocalMeta.setCountry("country");
        hincLocalMeta.setGroupName("group");
        hincLocalMeta.setUuid("id");

        testMessage.setPayload(hincLocalMeta.toJson());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] message = objectMapper.writeValueAsBytes(testMessage);
            rabbitTemplate.convertAndSend(globalInputExchange, "", message);
            return new ResponseEntity("sent \n" + testMessage.toJson(), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/resource")
    public ResponseEntity<String> testupdate() {
        Resource resource = new Resource();
        resource.setUuid("testresource");
        resource.setName("test-Name");
        resource.setLocation("test-Location");
        resource.setPluginName("test-PluginName");
        resource.setResourceType(Resource.ResourceType.IOT_RESOURCE);

        resourceRepository.save(resource);

        return new ResponseEntity<>("inserted testresource", HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity get(){
        HincLocalMeta hincLocalMeta = hincLocalMetaRepository.findById("id").get();
        return new ResponseEntity<>(hincLocalMeta.toJson(), HttpStatus.OK);
    }



    @GetMapping("/greeting")
    public ResponseEntity greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return new ResponseEntity<>("greeting", HttpStatus.OK);
    }


}
